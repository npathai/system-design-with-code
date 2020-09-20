package org.npathai.discourse.application.domain.users;

import java.util.Optional;

public class MySqlUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> getById(String userId) {
        throw new UnsupportedOperationException();
    }
}
