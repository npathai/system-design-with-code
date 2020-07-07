package org.npathai.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
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
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.npathai.dao.AnalyticsDao;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.InMemoryAnalyticsDao;
import org.npathai.model.AnalyticsInfo;
import org.npathai.util.jwt.JWTCreator;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class AnalyticsAPITest {
    public static final String REDIRECTION_ID = "AAAAA";

    @Inject
    @Client(value = "/")
    RxHttpClient httpClient;

    @Inject
    SecretSignatureConfiguration secretSignatureConfiguration;

    SignedJWT accessToken;

    @MockBean(AnalyticsDao.class)
    public AnalyticsDao createInMemoryDao() {
        return new InMemoryAnalyticsDao();
    }

    @BeforeEach
    public void initialize() throws JOSEException, DataAccessException {
        accessToken = new JWTCreator(secretSignatureConfiguration.getSecret()).createJwtForRoot();
    }

    // FIXME add this test back, once approach is decided to address this issue
    @Disabled
    @Test
    public void givesUnauthorizedAccessResponseWhenSomeoneOtherThanCreatorAccessesTheAnalyticsEndpoint() throws JOSEException {
        SignedJWT nonAuthorJwt = new JWTCreator(secretSignatureConfiguration.getSecret())
                .createJwtFor(UUID.randomUUID().toString(), "hacker");

        httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + REDIRECTION_ID)).blockingFirst();

        HttpResponse<AnalyticsInfo> response = httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/analytics/" + REDIRECTION_ID)
                        .headers(Map.of("Authorization", "Bearer " + nonAuthorJwt.serialize())),
                AnalyticsInfo.class).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());
    }

    @Test
    public void givesUnauthorizedAccessWhenAnonymousRequestIsReceived() {
        Assertions.assertThatThrownBy(() -> httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/analytics/" +
                REDIRECTION_ID), AnalyticsInfo.class).blockingFirst())
                .isInstanceOf(HttpClientException.class)
                .hasMessage("Unauthorized");
    }

    @Test
    public void incrementsClickCountByOne() {
        HttpResponse<Void> createResponse = httpClient.exchange(HttpRequest.create(HttpMethod.POST, "/analytics/"
                        + REDIRECTION_ID).headers(Map.of("Authorization", "Bearer "
                        + accessToken.serialize())),
                Void.class).blockingFirst();

        assertThat(createResponse.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        HttpResponse<Void> clickResponse = httpClient.exchange(HttpRequest.create(HttpMethod.POST, "/analytics/"
                        + REDIRECTION_ID + "/click").headers(Map.of("Authorization", "Bearer "
                        + accessToken.serialize())),
                Void.class).blockingFirst();

        assertThat(clickResponse.status().getCode()).isEqualTo(HttpStatus.OK.getCode());

        HttpResponse<AnalyticsInfo> infoResponse = httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/analytics/"
                        + REDIRECTION_ID).headers(Map.of("Authorization", "Bearer "
                        + accessToken.serialize())),
                AnalyticsInfo.class).blockingFirst();
        assertThat(infoResponse.status().getCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(infoResponse.getBody()).map(info -> info.getClickCount()).hasValue(1);
    }

}