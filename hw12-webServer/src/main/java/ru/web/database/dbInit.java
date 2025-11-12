package ru.web.database;

import org.hibernate.cfg.Configuration;
import ru.web.database.core.repository.DataTemplateHibernate;
import ru.web.database.core.repository.HibernateUtils;
import ru.web.database.core.sessionmanager.TransactionManagerHibernate;
import ru.web.database.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.web.database.crm.model.Address;
import ru.web.database.crm.model.Client;
import ru.web.database.crm.model.Phone;
import ru.web.database.crm.service.DBServiceClient;
import ru.web.database.crm.service.DbServiceClientImpl;

public class dbInit {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private final DBServiceClient dbServiceClient;

    public dbInit() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
    }

    public DBServiceClient getServiceClient() {
        return dbServiceClient;
    }
}
