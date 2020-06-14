package org.npathai;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.env.Environment;
import org.npathai.cache.RedirectionCache;
import org.npathai.cache.RedisRedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.dao.MySqlRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.domain.UrlShortener;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.time.Clock;
import java.util.Properties;

@Factory
public class ObjectMother {

    @Inject
    Environment environment;

    @Inject
    BeanContext beanContext;

    @Singleton
     public ZkManager createZkManager() throws InterruptedException {
        DefaultZkManagerFactory zkManagerFactory = new DefaultZkManagerFactory();
        return zkManagerFactory.createConnected(environment.getProperty("zookeeperUrl", String.class).get());
    }

    @Singleton
    public RedirectionCache redirectionCache() {
        Properties properties = new Properties();
        properties.put("redisUrl",
                environment.getProperty("redisUrl", String.class).get());
        return new RedisRedirectionCache(properties);
    }

    @Singleton
    public RedirectionDao redirectionDao() throws SQLException {
        Properties properties = new Properties();
        properties.put("mysql.user",
                environment.getProperty("mysql.user", String.class).get());
        properties.put("mysql.password",
                environment.getProperty("mysql.password", String.class).get());
        properties.put("mysql.url",
                environment.getProperty("mysql.url", String.class).get());

        return new MySqlRedirectionDao(properties);
    }

    @Singleton
    public UrlShortener urlShortener() {
        Properties properties = new Properties();
        properties.put("anonymousUrlLifetimeInSeconds",
                environment.getProperty("anonymousUrlLifetimeInSeconds", String.class).get());

        return new UrlShortener(properties,
                beanContext.getBean(IdGenerationServiceClient.class),
                beanContext.getBean(RedirectionDao.class),
                beanContext.getBean(RedirectionCache.class),
                Clock.systemDefaultZone());
    }
}
