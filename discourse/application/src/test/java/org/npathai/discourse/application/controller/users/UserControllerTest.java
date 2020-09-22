package org.npathai.discourse.application.controller.users;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.npathai.discourse.application.domain.users.RegistrationData;
import org.npathai.discourse.application.domain.users.User;
import org.npathai.discourse.application.domain.users.UserService;
import org.npathai.discourse.application.domain.users.UsernameAlreadyExistsException;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@MicronautTest
public class UserControllerTest {

    @Inject
    UserControllerClient userClient;

    @Inject
    UserService userService;

    @Captor
    ArgumentCaptor<RegistrationData> registrationDataArgumentCaptor;

    private RegistrationData registrationData;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registrationData = createRegistrationData();
        user = userFrom(registrationData);
        when(userService.create(any(RegistrationData.class))).thenReturn(user);
    }

    private User userFrom(RegistrationData registrationData) {
        User user = new User();
        user.setUserId("1234");
        user.setUsername(registrationData.getUsername());
        user.setEmail(registrationData.getEmail());
        user.setName(registrationData.getName());
        user.setPassword(registrationData.getPassword());
        return user;
    }

    @Test
    public void createsUser() {
        HttpResponse<User> response = userClient.create(registrationData);

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.OK.getCode());
        verify(userService).create(registrationDataArgumentCaptor.capture());
        assertReflectionEquals(registrationData, registrationDataArgumentCaptor.getValue());
    }

    @Test
    public void returnsUserModelRepresentingTheNewlyCreatedUser() {
        HttpResponse<User> response = userClient.create(registrationData);

        assertReflectionEquals(user, response.body());
    }

    @Test
    public void informsUsernameIsDuplicate() {
        when(userService.create(any(RegistrationData.class))).thenThrow(new UsernameAlreadyExistsException());

        HttpClientResponseException exception = catchThrowableOfType(() -> userClient.create(registrationData),
                HttpClientResponseException.class);
        assertThat(exception.getStatus().getCode()).isEqualTo(400);
    }

    private RegistrationData createRegistrationData() {
        RegistrationData registrationData = new RegistrationData();
        registrationData.setUsername("buddha");
        registrationData.setPassword("asdf@123");
        registrationData.setName("Siddhartha Gautama");
        registrationData.setEmail("buddha@peace.in");
        return registrationData;
    }

    @MockBean(UserService.class)
    public UserService createMockUserService() {
        return Mockito.mock(UserService.class);
    }
}
