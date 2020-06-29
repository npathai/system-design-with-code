package org.npathai.controller;

import io.micronaut.http.*;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import io.reactivex.Flowable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.npathai.IdGenerationServiceStub;
import org.npathai.cache.InMemoryRedirectionCache;
import org.npathai.cache.RedirectionCache;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@MicronautTest
class RedirectionControllerTest {

    private static final String ID = "AAAAA";
    private static final String LONG_URL = "www.google.com";
    public static final Redirection REDIRECTION = new Redirection(ID, LONG_URL, System.currentTimeMillis(),
            System.currentTimeMillis() + 10000);

    @Inject
    IdGenerationServiceStub idGenerationServiceStub;

    @Inject
    @Client(value = "/", configuration = NoRedirectionConfiguration.class)
    RxHttpClient httpClient;

    @Inject
    RedirectionDao redirectionDao;

    @Inject
    RedirectionListener redirectionListener;

    @BeforeEach
    public void setUp() throws DataAccessException {
        idGenerationServiceStub.setId(ID);
        redirectionDao.save(REDIRECTION);
    }

    @Test
    public void hasAnonymousAccess() {
        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + ID), String.class);

        HttpResponse<String> redirectedResponse = response.blockingFirst();
        assertThat(redirectedResponse.status().getCode()).isNotEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    public void redirectsToLongUrl() {
        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + ID), String.class);

        HttpResponse<String> redirectedResponse = response.blockingFirst();
        assertThat(redirectedResponse.status().getCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.getCode());
        assertThat(redirectedResponse.header(HttpHeaders.LOCATION)).isEqualTo(LONG_URL);
    }

    @Test
    public void invokesRedirectionListenerOnSuccessfulRedirection() {
        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + ID), String.class);

        response.blockingFirst();

        verify(redirectionListener).onSuccessfulRedirection(any(HttpRequest.class),
                eq(ID), eq(REDIRECTION.longUrl()));
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

    @MockBean(RedirectionListener.class)
    public RedirectionListener createMockListener() {
        return Mockito.mock(RedirectionListener.class);
    }
}