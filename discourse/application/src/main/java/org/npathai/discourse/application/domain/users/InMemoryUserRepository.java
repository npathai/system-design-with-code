package org.npathai.discourse.application.domain.users;

import org.npathai.discourse.application.common.IdGenerator;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> userById;
    private IdGenerator idGenerator;

    public InMemoryUserRepository(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        this.userById = new ConcurrentHashMap<>();
    }

    @Override
    public void save(User user) {
        user.setUserId(idGenerator.nextId());

        userById.put(user.getUserId(), user);
    }

    @Override
    public Optional<User> getById(String userId) {
        return Optional.ofNullable(userById.get(userId));
    }
}
