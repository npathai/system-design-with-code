package org.npathai.cache;

import org.npathai.model.Redirection;

import java.util.HashMap;
import java.util.Optional;

public class InMemoryRedirectionCache implements RedirectionCache {
    private HashMap<String, Redirection> cache = new HashMap<>();

    @Override
    public Optional<Redirection> getById(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public void put(Redirection redirection) {
        cache.put(redirection.id(), redirection);
    }

    @Override
    public void deleteById(String id) {
        cache.remove(id);
    }
}
