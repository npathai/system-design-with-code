package org.npathai.cache;

import java.util.Optional;

public interface RedirectionCache {

    Optional<String> get(String id);
    /**
     * TODO Temporary hack to continue with usage of Jedis. Long term option will be to switch to Redisson
     */
    Optional<Long> getExpiryAtMillis(String id);

    void put(String id, String longUrl, long expiryAtMillis);

    void delete(String id);
}
