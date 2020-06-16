package org.npathai.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.cache.InMemoryRedirectionCache;
import org.npathai.cache.RedirectionCache;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class UrlShortenerAPITest {

    public static final String ANY_ID = "AAAAA";
    public static final String LONG_URL = "www.google.com";

    @Inject
    UrlShortenerClient client;

    @Inject
    IdGenerationServiceStub idGenerationServiceStub;

    @Inject
    LoginClient loginClient;

    @Inject
    @Client("/")
    RxHttpClient httpClient;

    BearerAccessRefreshToken bearerAccessRefreshToken;

    @BeforeEach
    public void setUp() {
        idGenerationServiceStub.setId(ANY_ID);
    }

    @Test
    public void canShortenAnonymousUrlRequests() {
        ShortenRequest shortenRequest = shortenRequest();

        String response = client.shorten(shortenRequest);

        assertCreatedResponse(response);
    }

    @Test
    public void canShortenAuthenticatedUrlRequests() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("root", "root");
        bearerAccessRefreshToken = loginClient.login(credentials);

        Flowable<String> result = httpClient.retrieve(HttpRequest.create(HttpMethod.POST, "/shorten")
                .headers(Map.of("Authorization", "Bearer " + bearerAccessRefreshToken.getAccessToken()))
                .body(shortenRequest()));

        String response = result.blockingFirst();
        assertCreatedResponse(response);
    }

    private static void assertCreatedResponse(String response) {
        JsonObject parsedResponse = Json.parse(response).asObject();
        assertThat(parsedResponse.get("id").asString()).isEqualTo(ANY_ID);
        assertThat(parsedResponse.get("longUrl").asString()).isEqualTo(LONG_URL);
        assertThat(parsedResponse.get("createdAt").asLong()).isCloseTo(System.currentTimeMillis(),
                Offset.offset(1000L));
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