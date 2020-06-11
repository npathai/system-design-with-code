package org.npathai.dao;

import org.npathai.model.Redirection;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRedirectionDao implements RedirectionDao {
    private Map<String, Redirection> idToRedirection = new ConcurrentHashMap<>();

    @Override
    public void save(Redirection redirection) {
        idToRedirection.put(redirection.id(), redirection);
    }

    @Override
    public Optional<Redirection> getById(String id) {
        return Optional.ofNullable(idToRedirection.get(id));
    }

    @Override
    public void deleteById(String id) {
        idToRedirection.remove(id);
    }

}
