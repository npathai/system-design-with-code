package org.npathai.dao;

import org.npathai.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> getUserByName(String username) throws DataAccessException;
}
