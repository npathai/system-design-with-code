package org.npathai.cache;

import com.google.common.base.Preconditions;
import org.npathai.model.Redirection;
import org.npathai.properties.ApplicationProperties;
import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class RedisRedirectionCache implements RedirectionCache, Closeable {

    // TODO Use Reddisson client library because it makes it easy to store custom objects in cache
    private final Jedis jedis;

    public RedisRedirectionCache(Properties applicationProperties) {
        jedis = new Jedis(Objects.requireNonNull(
                applicationProperties.getProperty(ApplicationProperties.REDIS_URL.name())));
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
        jedis.set(createdAtKey(redirection.id()), String.valueOf(redirection.createdAt()));
        jedis.set(expiryAtKey(redirection.id()), String.valueOf(redirection.expiryTimeInMillis()));
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
