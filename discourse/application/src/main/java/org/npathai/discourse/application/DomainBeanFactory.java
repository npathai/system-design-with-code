package org.npathai.discourse.application;

import io.micronaut.context.annotation.Bean;
import org.npathai.discourse.application.domain.users.UserService;

public class DomainBeanFactory {

    @Bean
    public UserService createUserService() {
        return new UserService();
    }
}
