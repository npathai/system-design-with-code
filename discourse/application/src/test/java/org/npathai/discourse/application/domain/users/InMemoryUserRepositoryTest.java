package org.npathai.discourse.application.domain.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.npathai.discourse.application.common.IdGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class InMemoryUserRepositoryTest {

    private static final String USER_ID = "1234";

    @Mock
    IdGenerator idGenerator;

    InMemoryUserRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = new InMemoryUserRepository(idGenerator);
        when(idGenerator.nextId()).thenReturn(USER_ID);
    }

    @Test
    public void assignsIdToUser() {
        User user = createUser();
        repository.save(user);

        assertThat(user.getUserId()).isEqualTo(USER_ID);
    }

    @Test
    public void returnsSavedUserById() {
        User user = createUser();

        repository.save(user);

        assertThat(repository.getById(user.getUserId()))
                .isPresent()
                .hasValue(user);
    }

    private User createUser() {
        User user = new User();
        user.setUserId(USER_ID);
        user.setUsername("buddha");
        user.setEmail("buddha@peace.in");
        user.setName("Siddhartha Gautama");
        user.setPassword("asdf@123");
        return user;
    }
}