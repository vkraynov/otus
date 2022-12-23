package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;

public class HomeworkDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger log = LoggerFactory.getLogger(HomeworkDemo.class);

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        Client savedClient = dbServiceClient.saveClient(client);

        Client clientLoaded = dbServiceClient.getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));
        log.info("clientSecondSelected:{}", clientLoaded);

        log.info("All clients");
        dbServiceClient.findAll().forEach(c -> log.info("client:{}", c));

    }
}
