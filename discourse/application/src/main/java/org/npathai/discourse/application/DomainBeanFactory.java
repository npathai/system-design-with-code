package org.npathai.discourse.application;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.npathai.discourse.application.common.IdGenerator;
import org.npathai.discourse.application.domain.users.InMemoryUserRepository;
import org.npathai.discourse.application.domain.users.UserRepository;
import org.npathai.discourse.application.domain.users.UserService;

import javax.inject.Inject;

@Factory
public class DomainBeanFactory {

    @Inject
    BeanContext beanContext;

    @Bean
    public IdGenerator createIdGenerator() {
        return new IdGenerator();
    }

    @Bean
    public UserRepository createUserRepository() {
        return new InMemoryUserRepository(beanContext.getBean(IdGenerator.class));
    }

    @Bean
    public UserService createUserService() {
        return new UserService(beanContext.getBean(UserRepository.class));
    }
}
