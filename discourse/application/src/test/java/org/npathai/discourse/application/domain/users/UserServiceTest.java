package org.npathai.discourse.application.domain.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Mockito.verify;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

class UserServiceTest {

    public static final String USER_ID = "1234";
    UserService userService;

    @Spy
    UserRepositoryStub userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void createANewUser() {
        RegistrationData registrationData = createRegistrationData();
        User expectedUser = userFrom(registrationData);
        expectedUser.setUserId(USER_ID);
        userRepository.setUserId(USER_ID);

        User actualUser = userService.create(registrationData);

        verify(userRepository).save(actualUser);
    }

    @Test
    public void returnNewlyCreatedUser() {
        RegistrationData registrationData = createRegistrationData();
        User expectedUser = userFrom(registrationData);
        expectedUser.setUserId(USER_ID);
        userRepository.setUserId(USER_ID);

        User actualUser = userService.create(registrationData);

        assertReflectionEquals(expectedUser, actualUser);
    }

    private RegistrationData createRegistrationData() {
        RegistrationData registrationData = new RegistrationData();
        registrationData.setUsername("buddha");
        registrationData.setName("Siddhartha Gautama");
        registrationData.setPassword("asdf@1234");
        registrationData.setEmail("buddha@peace.in");
        return registrationData;
    }

    private User userFrom(RegistrationData registrationData) {
        User user = new User();
        user.setUserId(USER_ID);
        user.setUsername(registrationData.getUsername());
        user.setEmail(registrationData.getEmail());
        user.setName(registrationData.getName());
        user.setPassword(registrationData.getPassword());
        return user;
    }
}