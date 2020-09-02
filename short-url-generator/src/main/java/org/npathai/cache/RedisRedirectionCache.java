package org.npathai.cache;

import com.google.common.base.Preconditions;
import org.npathai.config.RedisConfiguration;
import org.npathai.model.Redirection;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.Closeable;
import java.util.Optional;

public class RedisRedirectionCache implements RedirectionCache, Closeable {

    final RedissonClient redissonClient;
    private final RMap<String, String> cache;

    public RedisRedirectionCache(RedisConfiguration redisConfiguration) {
        Config config = new Config();
        config.useSingleServer().setAddress(redisConfiguration.getUrl());
        redissonClient = Redisson.create(config);
        cache = redissonClient.getMap("urlCache");
    }

    @Override
    public Optional<Redirection> getById(String id) {
        String longUrl = cache.get(redirectionIdKey(id));
        if (longUrl == null) {
            return Optional.empty();
        }
        String createdAt = cache.get(createdAtKey(id));
        Preconditions.checkState(createdAt != null,
                "createdAt must be present in cache if long url is present");
        String expiryAtMillis = cache.get(expiryAtKey(id));
        Preconditions.checkState(expiryAtMillis != null,
                "expiryAtMillis must be present in cache if long url is present");

        return Optional.of(new Redirection(id, longUrl, Long.parseLong(createdAt), Long.parseLong(expiryAtMillis)));
    }

    @Override
    public void put(Redirection redirection) {
        cache.put(redirectionIdKey(redirection.id()), redirection.longUrl());
        cache.put(createdAtKey(redirection.id()), String.valueOf(redirection.createdAtMillis()));
        cache.put(expiryAtKey(redirection.id()), String.valueOf(redirection.expiryAtMillis()));
    }

    private String createdAtKey(String id) {
        return "createdAt#" + id;
    }

    private String expiryAtKey(String id) {
        return "expiryAt#" + id;
    }


    private String redirectionIdKey(String id) {
        return "redirection#" + id;
    }

    @Override
    public void deleteById(String id) {
        cache.remove(redirectionIdKey(id));
        cache.remove(createdAtKey(id));
        cache.remove(expiryAtKey(id));
    }

    @Override
    public void close() {
        redissonClient.shutdown();
    }
}
