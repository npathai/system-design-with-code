package org.npathai.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.nimbusds.jose.JOSEException;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.signature.secret.SecretSignatureConfiguration;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.IdGenerationServiceStub;
import org.npathai.cache.InMemoryRedirectionCache;
import org.npathai.cache.RedirectionCache;
import org.npathai.config.UrlLifetimeConfiguration;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.util.jwt.JWTCreator;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class UrlShortenerAPITest {

    public static final String ANY_ID = "AAAAA";
    public static final String LONG_URL = "www.google.com";

    @Inject
    UrlLifetimeConfiguration urlLifetimeConfiguration;

    @Inject
    IdGenerationServiceStub idGenerationServiceStub;

    @Inject
    @Client("/")
    RxHttpClient httpClient;

    @Inject
    SecretSignatureConfiguration secretSignatureConfiguration;

    @BeforeEach
    public void setUp() {
        idGenerationServiceStub.setId(ANY_ID);
    }

    @Test
    public void canShortenAnonymousUrlRequests() {
        Flowable<String> result = httpClient.retrieve(HttpRequest.create(HttpMethod.POST, "/shorten")
                .body(shortenRequest()));

        String response = result.blockingFirst();
        assertCreatedResponse(response, Duration.ofSeconds(
                Long.parseLong(urlLifetimeConfiguration.getAnonymous())
        ));
    }

    @Test
    public void canShortenAuthenticatedUrlRequests() throws JOSEException {
        String accessToken = createJwtToken();

        Flowable<String> result = httpClient.retrieve(HttpRequest.create(HttpMethod.POST, "/shorten")
                .headers(Map.of("Authorization", "Bearer " + accessToken))
                .body(shortenRequest()));

        String response = result.blockingFirst();
        assertCreatedResponse(response, Duration.ofSeconds(
                Long.parseLong(urlLifetimeConfiguration.getAuthenticated())
        ));
    }

    private String createJwtToken() throws JOSEException {
        return new JWTCreator(secretSignatureConfiguration.getSecret())
                .createJwtForRoot().serialize();
    }

    private static void assertCreatedResponse(String response, Duration expectedLifetime) {
        JsonObject parsedResponse = Json.parse(response).asObject();
        assertThat(parsedResponse.get("id").asString()).isEqualTo(ANY_ID);
        assertThat(parsedResponse.get("longUrl").asString()).isEqualTo(LONG_URL);
        long createdAtMillis = parsedResponse.get("createdAt").asLong();
        assertThat(createdAtMillis).isCloseTo(System.currentTimeMillis(),
                Offset.offset(1000L));
        long expiryAtMillis = parsedResponse.get("expiryAt").asLong();
        assertThat(expiryAtMillis).isGreaterThan(createdAtMillis);
        Duration actualLifetime = Duration.ofMillis(expiryAtMillis - createdAtMillis);
        assertThat(actualLifetime).isEqualTo(expectedLifetime);
    }

    private ShortenRequest shortenRequest() {
        ShortenRequest shortenRequest = new ShortenRequest();
        shortenRequest.setLongUrl(LONG_URL);
        return shortenRequest;
    }

    @MockBean(ZkManager.class)
    public ZkManager createTestingZkManager() {
        return new TestingZkManager();
    }

    @MockBean(RedirectionCache.class)
    public RedirectionCache createInMemoryCache() {
        return new InMemoryRedirectionCache();
    }

    @MockBean(RedirectionDao.class)
    public RedirectionDao createInMemoryDao() {
        return new InMemoryRedirectionDao();
    }


}