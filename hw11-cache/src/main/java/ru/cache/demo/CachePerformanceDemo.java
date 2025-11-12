package ru.cache.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cache.cachehw.HwCache;
import ru.cache.cachehw.HwListener;
import ru.cache.cachehw.MyCache;
import ru.cache.core.repository.DataTemplateHibernate;
import ru.cache.core.repository.HibernateUtils;
import ru.cache.core.sessionmanager.TransactionManagerHibernate;
import ru.cache.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.cache.crm.model.Address;
import ru.cache.crm.model.Client;
import ru.cache.crm.model.Phone;
import ru.cache.crm.service.CachedDBServiceClient;
import ru.cache.crm.service.DBServiceClient;
import ru.cache.crm.service.DbServiceClientImpl;

public class CachePerformanceDemo {
    private static final Logger log = LoggerFactory.getLogger(CachePerformanceDemo.class);
    private static final int ITERATIONS = 100;
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";


    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        DBServiceClient dbService = new DbServiceClientImpl(transactionManager, clientTemplate);
        HwCache<Long, Client> cache = createCacheWithLogging();
        DBServiceClient cachedService = new CachedDBServiceClient(dbService, cache);

        log.info("Подготовка данных");
        List<Long> clientIds = prepareTestData(dbService, 100);

        log.info("Тест производительности");
        testPerformance(dbService, cachedService, clientIds);
    }


    private static HwCache<Long, Client> createCacheWithLogging() {
        HwCache<Long, Client> cache = new MyCache<>();

        HwListener<Long, Client> listener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.debug("Cache event - Key: {}, Action: {}, Value: {}", key, action, value);
            }
        };

        cache.addListener(listener);
        return cache;
    }


    private static List<Long> prepareTestData(DBServiceClient service, int count) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Client client = new Client("Client_" + i);
            Client saved = service.saveClient(client);
            ids.add(saved.getId());
        }
        return ids;
    }


    private static void testPerformance(DBServiceClient withoutCache, DBServiceClient withCache, List<Long> clientIds) {

        log.info("Запуск теста без кеша.");
        long startTime = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            for (Long id : clientIds) {
                withoutCache.getClient(id);
            }
        }

        long withoutCacheTime = System.nanoTime() - startTime;

        log.info("Запуск теста с кешем.");
        startTime = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            for (Long id : clientIds) {
                withCache.getClient(id);
            }
        }

        long withCacheTime = System.nanoTime() - startTime;

        log.info("РЕЗУЛЬТАТЫ");
        log.info("Время без кеша: {} мс", TimeUnit.NANOSECONDS.toMillis(withoutCacheTime));
        log.info("Время с кешем: {} мс", TimeUnit.NANOSECONDS.toMillis(withCacheTime));
    }
}
