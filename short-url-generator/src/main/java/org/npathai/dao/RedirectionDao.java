package org.npathai.dao;

import org.npathai.model.Redirection;

import java.util.Optional;

public interface RedirectionDao {
    void save(Redirection redirection) throws DataAccessException;
    Optional<Redirection> getById(String id) throws DataAccessException;

    void deleteById(String id);
}
