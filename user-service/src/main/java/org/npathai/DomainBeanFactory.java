package org.npathai;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Factory;
import org.npathai.config.MySqlDatasourceConfiguration;
import org.npathai.dao.MySqlUserDao;
import org.npathai.dao.UserDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class DomainBeanFactory {

    @Inject
    BeanContext beanContext;

    @Singleton
    public UserDao createMySqlUserDao() {
        return new MySqlUserDao(beanContext.getBean(MySqlDatasourceConfiguration.class));
    }
}
