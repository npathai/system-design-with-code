package org.npathai.cache;

import org.npathai.config.RedisConfiguration;
import org.npathai.model.Redirection;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.io.Closeable;
import java.util.Optional;

public class RedisRedirectionCache implements RedirectionCache, Closeable {

    final RedissonClient redissonClient;
    private final RMap<String, Redirection> cache;

    public RedisRedirectionCache(RedisConfiguration redisConfiguration) {
        Config config = new Config();
        config.setCodec(new JsonJacksonCodec());
        config.useSingleServer().setAddress(redisConfiguration.getUrl());
        redissonClient = Redisson.create(config);
        cache = redissonClient.getMap("urlCache");
    }

    @Override
    public Optional<Redirection> getById(String id) {
        Redirection redirection = cache.get(id);
        if (redirection == null) {
            return Optional.empty();
        }
        return Optional.of(redirection);
    }

    @Override
    public void put(Redirection redirection) {
        cache.put(redirection.id(), redirection);
    }

    @Override
    public void deleteById(String id) {
        cache.remove(id);
    }

    @Override
    public void close() {
        redissonClient.shutdown();
    }
}
