package org.npathai.cache;

import java.util.Optional;

public interface RedirectionCache {

    Optional<String> get(String id);
    void put(String id, String longUrl);
}
