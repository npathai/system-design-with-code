package org.npathai.discourse.application;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.npathai.discourse.application.domain.users.MySqlUserRepository;
import org.npathai.discourse.application.domain.users.UserRepository;
import org.npathai.discourse.application.domain.users.UserService;

import javax.inject.Inject;

@Factory
public class DomainBeanFactory {

    @Inject
    BeanContext beanContext;

    @Bean
    public UserRepository createUserRepository() {
        return new MySqlUserRepository();
    }

    @Bean
    public UserService createUserService() {
        return new UserService(beanContext.getBean(UserRepository.class));
    }
}
