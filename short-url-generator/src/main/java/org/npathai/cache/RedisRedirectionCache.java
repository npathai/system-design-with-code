package org.npathai.cache;

import org.npathai.properties.ApplicationProperties;
import redis.clients.jedis.Jedis;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class RedisRedirectionCache implements RedirectionCache, Closeable {

    // TODO maybe Redisson client library?
    private final Jedis jedis;

    public RedisRedirectionCache(Properties applicationProperties) {
        jedis = new Jedis(Objects.requireNonNull(
                applicationProperties.getProperty(ApplicationProperties.REDIS_URL.name())));
        jedis.connect();
    }

    @Override
    public Optional<String> get(String id) {
        return Optional.ofNullable(jedis.get(redirectionId(id)));
    }

    @Override
    public Optional<Long> getExpiryAtMillis(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void put(String id, String longUrl, long expiryAtMillis) {
        jedis.set(redirectionId(id), longUrl);
    }

    private String redirectionId(String id) {
        return "redirection#" + id;
    }

    @Override
    public void delete(String id) {
        jedis.del(redirectionId(id));
    }

    @Override
    public void close() throws IOException {
        jedis.close();
    }
}
