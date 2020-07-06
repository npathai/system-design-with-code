package org.npathai;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import org.npathai.dao.AnalyticsDao;
import org.npathai.dao.InMemoryAnalyticsDao;
import org.npathai.domain.AnalyticsService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class DomainBeanFactory {

    @Inject
    BeanContext beanContext;

    @Singleton
    public AnalyticsDao analyticsDao() {
        return new InMemoryAnalyticsDao();
    }

    @Singleton
    public AnalyticsService analyticsService() {
        return new AnalyticsService(beanContext.getBean(AnalyticsDao.class));
    }
}
