package org.npathai.cache;

import org.npathai.model.Redirection;

import java.util.Optional;

public interface RedirectionCache {

    Optional<Redirection> getById(String id);

    void put(Redirection redirection);

    void deleteById(String id);
}
