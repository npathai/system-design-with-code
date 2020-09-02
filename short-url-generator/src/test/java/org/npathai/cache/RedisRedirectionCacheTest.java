package org.npathai.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.config.RedisConfiguration;
import org.npathai.model.Redirection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class RedisRedirectionCacheTest {

    private static final long CREATION_TIME = System.currentTimeMillis();
    public static final String LONG_URL = "www.google.com";
    private static final String ID = "AAAAA";
    private static final String UNKNOWN_ID = "UNKNOWN_ID";
    private static final String USER_ID = "test";

    public static final Redirection ORIGINAL_REDIRECTION = new Redirection(ID, LONG_URL, CREATION_TIME,
            CREATION_TIME + 1000, USER_ID);

    @Container
    public GenericContainer<?> redis = new GenericContainer<>("redis:latest")
            .withExposedPorts(6379);
    private RedisRedirectionCache redisRedirectionCache;

    @BeforeEach
    public void setUp() {
        RedisConfiguration redisConfiguration = new RedisConfiguration();
        String containerIpAddress = redis.getContainerIpAddress();
        int port = redis.getMappedPort(6379);
        redisConfiguration.setUrl(String.format("redis://%s:%d", containerIpAddress, port));
        redisRedirectionCache = new RedisRedirectionCache(redisConfiguration);
    }

    @Test
    public void canGetCachedRedirection() {
        redisRedirectionCache.put(ORIGINAL_REDIRECTION);

        Optional<Redirection> foundRedirection = redisRedirectionCache.getById(ID);
        assertThat(foundRedirection).isNotEmpty();
        ReflectionAssert.assertReflectionEquals(ORIGINAL_REDIRECTION, foundRedirection.get());
    }

    @Test
    public void returnsEmptyOptionalIfKeyIsNotPresentInCache() {
        assertThat(redisRedirectionCache.getById(UNKNOWN_ID)).isEmpty();
    }

    @Test
    public void deleteByIdRemovesTheElementFromCache() {
        redisRedirectionCache.put(ORIGINAL_REDIRECTION);

        redisRedirectionCache.deleteById(ORIGINAL_REDIRECTION.id());

        assertThat(redisRedirectionCache.getById(ORIGINAL_REDIRECTION.id())).isEmpty();
    }
}