package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HomeworkDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Logger log = LoggerFactory.getLogger(HomeworkDemo.class);

    public static void main(String[] args) throws InterruptedException {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Phone.class, Address.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        MyCache<String, Client> myCache = new MyCache<>();

        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        myCache.addListener(listener);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, myCache);

        var client = new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        Client savedClient = dbServiceClient.saveClient(client);

        LocalDateTime before = LocalDateTime.now();
        Client clientLoaded = dbServiceClient.getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));
        LocalDateTime after = LocalDateTime.now();
        System.out.println("Без кэша: " + Duration.between(before, after).toNanos());
        log.info("clientSecondSelected:{}", clientLoaded);

        before = LocalDateTime.now();
        Client clientLoadedFromCache = dbServiceClient.getClient(savedClient.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + savedClient.getId()));
        after = LocalDateTime.now();
        System.out.println("Из кэша: " + Duration.between(before, after).toNanos());
        log.info("clientLoadedFromCache:{}", clientLoadedFromCache);

        for (int i = 0; i < 100; i++) {
            dbServiceClient.saveClient(new Client(null, "Vasya", new Address(null, "AnyStreet"), List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));
        }

        for (int i = 1; i < 100; i++) {
            dbServiceClient.getClient(i);
        }
        log.info("Cache size:{}", myCache.getCacheSize());

        System.gc();
        Thread.sleep(100);

        log.info("Cache size after GC:{}", myCache.getCacheSize());
    }
}
