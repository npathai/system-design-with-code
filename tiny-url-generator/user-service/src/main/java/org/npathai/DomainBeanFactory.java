package org.npathai;

import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Factory;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.dao.MySqlUserDao;
import org.npathai.dao.UserDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class DomainBeanFactory {

    private BeanContext beanContext;

    public DomainBeanFactory(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    @Singleton
    public UserDao createMySqlUserDao() {
        return new MySqlUserDao(beanContext.getBean(MySqlDatasourceConfiguration.class),
                beanContext.getBean(MeterRegistry.class));
    }
}
