package org.npathai.cache;

import com.google.common.base.Preconditions;
import org.npathai.config.RedisConfiguration;
import org.npathai.model.Redirection;
import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.util.Objects;
import java.util.Optional;

public class RedisRedirectionCache implements RedirectionCache, Closeable {

    // TODO Use Reddisson client library because it makes it easy to store custom objects in cache
    private final Jedis jedis;

    public RedisRedirectionCache(RedisConfiguration redisConfiguration) {
        jedis = new Jedis(Objects.requireNonNull(
                redisConfiguration.getUrl()));
        jedis.connect();
    }

    @Override
    public Optional<Redirection> getById(String id) {
        String longUrl = jedis.get(redirectionIdKey(id));
        if (longUrl == null) {
            return Optional.empty();
        }
        String createdAt = jedis.get(createdAtKey(id));
        Preconditions.checkState(createdAt != null,
                "createdAt must be present in cache if long url is present");
        String expiryAtMillis = jedis.get(expiryAtKey(id));
        Preconditions.checkState(expiryAtMillis != null,
                "expiryAtMillis must be present in cache if long url is present");

        return Optional.of(new Redirection(id, longUrl, Long.parseLong(createdAt), Long.parseLong(expiryAtMillis)));
    }

    @Override
    public void put(Redirection redirection) {
        jedis.set(redirectionIdKey(redirection.id()), redirection.longUrl());
        jedis.set(createdAtKey(redirection.id()), String.valueOf(redirection.createdAtMillis()));
        jedis.set(expiryAtKey(redirection.id()), String.valueOf(redirection.expiryAtMillis()));
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
        jedis.del(redirectionIdKey(id));
        jedis.del(createdAtKey(id));
        jedis.del(expiryAtKey(id));
    }

    @Override
    public void close() {
        jedis.close();
    }
}
