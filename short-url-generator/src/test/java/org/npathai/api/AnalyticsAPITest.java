package org.npathai.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.core.io.buffer.ByteBuffer;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.security.token.jwt.signature.secret.SecretSignatureConfiguration;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.IdGenerationServiceStub;
import org.npathai.cache.InMemoryRedirectionCache;
import org.npathai.cache.RedirectionCache;
import org.npathai.controller.NoRedirectionConfiguration;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.AnalyticsInfo;
import org.npathai.model.Redirection;
import org.npathai.util.JWTCreator;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class AnalyticsAPITest {

    public static final Redirection REDIRECTION = new Redirection("AAAAA", "www.google.com",
            System.currentTimeMillis(), System.currentTimeMillis() + 10000, JWTCreator.USER_ID);

    @Inject
    IdGenerationServiceStub idGenerationServiceStub;

    @Inject
    @Client(value = "/", configuration = NoRedirectionConfiguration.class)
    RxHttpClient httpClient;

    @Inject
    SecretSignatureConfiguration secretSignatureConfiguration;

    SignedJWT accessToken;

    @Inject
    RedirectionDao redirectionDao;

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

    @BeforeEach
    public void initialize() throws JOSEException, DataAccessException {
        accessToken = new JWTCreator(secretSignatureConfiguration.getSecret()).createJwtForRoot();
        redirectionDao.save(REDIRECTION);
    }

    @Test
    public void givesUnauthorizedAccessResponseWhenSomeoneOtherThanCreatorAccessesTheAnalyticsEndpoint() throws JOSEException {
        SignedJWT nonAuthorJwt = new JWTCreator(secretSignatureConfiguration.getSecret())
                .createJwtFor(UUID.randomUUID().toString(), "hacker");

        httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + REDIRECTION.id())).blockingFirst();

        HttpResponse<AnalyticsInfo> response = httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/analytics/" + REDIRECTION.id())
                        .headers(Map.of("Authorization", "Bearer " + nonAuthorJwt.serialize())),
                AnalyticsInfo.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());
    }

    @Test
    public void givesUnauthorizedAccessWhenAnonymousRequestIsReceived() {
        httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + REDIRECTION.id())).blockingFirst();

        Assertions.assertThatThrownBy(() -> httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/analytics/" +
                REDIRECTION.id()), AnalyticsInfo.class).blockingFirst())
                .isInstanceOf(HttpClientException.class)
                .hasMessage("Unauthorized");
    }

    @Test
    public void incrementsClickCountByOneWhenRedirectionIsVisited() {
        HttpResponse<String> redirectionResponse = httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + REDIRECTION.id()), String.class).blockingFirst();
        assertThat(redirectionResponse.status().getCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.getCode());

        HttpResponse<AnalyticsInfo> response = httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/analytics/" + REDIRECTION.id())
                .headers(Map.of("Authorization", "Bearer " + accessToken.serialize())),
                AnalyticsInfo.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());
    }
}