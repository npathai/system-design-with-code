package org.npathai.discourse.application.domain.users;

public class UserRepositoryStub implements UserRepository {
    private String userId;

    @Override
    public void save(User user) {
        user.setUserId(userId);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
