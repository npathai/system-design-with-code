package org.npathai.discourse.application.domain.users;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> getById(String userId);
}
