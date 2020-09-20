package org.npathai.discourse.application.domain.users;

import java.util.Optional;

public class UserRepositoryStub implements UserRepository {
    private String userId;
    private User user;

    @Override
    public void save(User user) {
        this.user = user;
        user.setUserId(userId);
    }

    @Override
    public Optional<User> getById(String userId) {
        return Optional.of(user);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
