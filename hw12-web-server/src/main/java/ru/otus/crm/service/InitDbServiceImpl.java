package ru.otus.crm.service;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.model.User;

import java.util.List;

import static ru.otus.demo.WebServerWithFilterBasedSecurityDemo.HIBERNATE_CFG_FILE;

/**
 * InitDbServiceImpl.
 *
 * @author Vadim_Kraynov
 * @since 05.01.2023
 */
public class InitDbServiceImpl implements InitDbService {

    @Override
    public void initDb() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class, User.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var userTemplate = new DataTemplateHibernate<>(User.class);

        var dbServiceUser = new DbServiceUserImpl(transactionManager, userTemplate);

        List<Client> clients = List.of(new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))),
                new Client(null, "Petya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))),
                new Client(null, "Sergey", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));

        clients.forEach(dbServiceClient::saveClient);


        dbServiceUser.saveUser(new User(null, "Админ", "admin", "admin"));

    }
}
