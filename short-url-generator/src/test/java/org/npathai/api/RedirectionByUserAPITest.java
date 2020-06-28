package org.npathai.api;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.signature.secret.SecretSignatureConfiguration;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.IdGenerationServiceStub;
import org.npathai.cache.InMemoryRedirectionCache;
import org.npathai.cache.RedirectionCache;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.util.JWTCreator;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;
import org.unitils.reflectionassert.ReflectionAssert;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class RedirectionByUserAPITest {
    private static final String ANY_ID = "AAABB";
    private static final String LONG_URL = "www.google.com";

    @Inject
    @Client("/")
    RxHttpClient httpClient;

    @Inject
    SecretSignatureConfiguration secretSignatureConfiguration;

    @Inject
    RedirectionDao redirectionDao;

    SignedJWT accessToken;
    private Redirection olderRedirection;
    private Redirection newerRedirection;
    private Redirection expiredRedirection;

    @BeforeEach
    public void setUp() throws JOSEException, DataAccessException {
        accessToken = new JWTCreator(secretSignatureConfiguration.getSecret()).createJwtForRoot();

        expiredRedirection = new Redirection("AAAAC", LONG_URL + "/1", System.currentTimeMillis(),
                System.currentTimeMillis(), JWTCreator.USER_ID);
        redirectionDao.save(expiredRedirection);

        olderRedirection = new Redirection("AAAAA", LONG_URL, System.currentTimeMillis(),
                System.currentTimeMillis() + 10000, JWTCreator.USER_ID);
        redirectionDao.save(olderRedirection);

        newerRedirection = new Redirection("AAAAB", LONG_URL + "/1", System.currentTimeMillis(),
                System.currentTimeMillis() + 10000, JWTCreator.USER_ID);
        redirectionDao.save(newerRedirection);
    }

    @Test
    public void returnsAllRedirectionsCreatedByUserInDescendingOrderOfCreation() {
        Flowable<HttpResponse<List<Redirection>>> result = httpClient.exchange(
                HttpRequest.create(HttpMethod.GET, "/user/redirection_history")
                        .headers(Map.of("Authorization", "Bearer " + accessToken.serialize())),
                Argument.listOf(Redirection.class)
        );

        HttpResponse<List<Redirection>> response = result.blockingFirst();
        assertThat(response.status().getCode()).isEqualTo(200);
        ReflectionAssert.assertReflectionEquals(List.of(newerRedirection, olderRedirection), response.body());
    }
    
    @Test
    public void onlyReturnsActiveRedirectionsAndFiltersOutExpiredRedirections() {
        Flowable<HttpResponse<List<Redirection>>> result = httpClient.exchange(
                HttpRequest.create(HttpMethod.GET, "/user/redirection_history")
                        .headers(Map.of("Authorization", "Bearer " + accessToken.serialize())),
                Argument.listOf(Redirection.class)
        );

        HttpResponse<List<Redirection>> response = result.blockingFirst();
        ReflectionAssert.assertReflectionEquals(List.of(newerRedirection, olderRedirection), response.body());
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
