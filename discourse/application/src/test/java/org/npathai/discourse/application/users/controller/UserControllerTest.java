package org.npathai.discourse.application.users.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.npathai.discourse.application.domain.users.RegistrationData;
import org.npathai.discourse.application.domain.users.UserService;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@MicronautTest
public class UserControllerTest {

    @Inject
    UserControllerClient userClient;

    @Inject
    UserService userService;

    @Captor
    ArgumentCaptor<RegistrationData> registrationDataArgumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createsUser() {
        RegistrationData registrationData = new RegistrationData();
        registrationData.setUsername("buddha");
        registrationData.setPassword("asdf@123");
        registrationData.setName("Siddhartha Gautama");
        registrationData.setEmail("buddha@peace.in");

        HttpResponse<Void> response = userClient.create(registrationData);

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.OK.getCode());
        verify(userService).create(registrationDataArgumentCaptor.capture());
        assertReflectionEquals(registrationData, registrationDataArgumentCaptor.getValue());
    }

    @MockBean(UserService.class)
    public UserService createMockUserService() {
        return Mockito.mock(UserService.class);
    }
}
