package org.npathai.controller;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.nimbusds.jose.JOSEException;
import io.micronaut.http.*;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.signature.secret.SecretSignatureConfiguration;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.IdGenerationServiceStub;
import org.npathai.api.ShortenRequest;
import org.npathai.cache.InMemoryRedirectionCache;
import org.npathai.cache.RedirectionCache;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.util.JWTCreator;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class RedirectionControllerTest {

    private static final String ANY_ID = "AAAAA";
    private static final String LONG_URL = "www.google.com";

    @Inject
    IdGenerationServiceStub idGenerationServiceStub;

    @Inject
    @Client(value = "/", configuration = NoRedirectionConfiguration.class)
    RxHttpClient httpClient;

    @Inject
    SecretSignatureConfiguration secretSignatureConfiguration;

    @BeforeEach
    public void setUp() {
        idGenerationServiceStub.setId(ANY_ID);
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

    @Test
    public void redirectsToLongUrl() {
        Flowable<String> result = httpClient.retrieve(HttpRequest.create(HttpMethod.POST, "/shorten")
                .body(shortenRequest()));

        String shortenResponse = result.blockingFirst();
        JsonObject parsedResponse = Json.parse(shortenResponse).asObject();
        String id = parsedResponse.get("id").asString();

        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + id), String.class);

        HttpResponse<String> redirectedResponse = response.blockingFirst();
        assertThat(redirectedResponse.status().getCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.getCode());
        assertThat(redirectedResponse.header(HttpHeaders.LOCATION)).isEqualTo(LONG_URL);
    }

    private ShortenRequest shortenRequest() {
        ShortenRequest shortenRequest = new ShortenRequest();
        shortenRequest.setLongUrl(LONG_URL);
        return shortenRequest;
    }

    private String createJwtToken() throws JOSEException {
        return new JWTCreator(secretSignatureConfiguration.getSecret())
                .createJwtForRoot().serialize();
    }
}