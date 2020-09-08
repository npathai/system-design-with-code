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
import org.npathai.client.AnalyticsServiceClient;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MicronautTest
class RedirectionControllerTest {

    private static final String ANONYMOUS_URL_ID = "AAAAA";
    private static final String NON_ANONYMOUS_URL_ID = "AAAAB";
    private static final String LONG_URL = "www.google.com";
    public static final Redirection ANONYMOUS_REDIRECTION = new Redirection(ANONYMOUS_URL_ID, LONG_URL, System.currentTimeMillis(),
            System.currentTimeMillis() + 10000);
    public static final Redirection NON_ANONYMOUS_REDIRECTION = new Redirection(NON_ANONYMOUS_URL_ID, LONG_URL, System.currentTimeMillis(),
            System.currentTimeMillis() + 10000, "test");


    @Inject
    IdGenerationServiceStub idGenerationServiceStub;

    @Inject
    @Client(value = "/", configuration = NoRedirectionConfiguration.class)
    RxHttpClient httpClient;

    @Inject
    RedirectionDao redirectionDao;

    @Inject
    AnalyticsServiceClient analyticsServiceClient;

    @BeforeEach
    public void setUp() throws DataAccessException {
        idGenerationServiceStub.setId(ANONYMOUS_URL_ID);
        redirectionDao.save(ANONYMOUS_REDIRECTION);
    }

    @Test
    public void hasAnonymousAccess() {
        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + ANONYMOUS_URL_ID), String.class);

        HttpResponse<String> redirectedResponse = response.blockingFirst();
        assertThat(redirectedResponse.status().getCode()).isNotEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    public void redirectsToLongUrl() {
        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + ANONYMOUS_URL_ID), String.class);

        HttpResponse<String> redirectedResponse = response.blockingFirst();
        assertThat(redirectedResponse.status().getCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.getCode());
        assertThat(redirectedResponse.header(HttpHeaders.LOCATION)).isEqualTo(LONG_URL);
    }

    @Test
    public void doesNotInvokeAnalyticsAPIForIncrementingClickCountOnSuccessfulRedirectionOfAnonymousUrl() {
        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + ANONYMOUS_URL_ID), String.class);

        response.blockingFirst();

        verify(analyticsServiceClient, never()).redirectionClicked(ANONYMOUS_URL_ID);
    }

    @Test
    public void invokesAnalyticsAPIForIncrementingClickCountOnSuccessfulRedirectionOfNonAnonymousUrl() throws DataAccessException {
        idGenerationServiceStub.setId(NON_ANONYMOUS_URL_ID);
        redirectionDao.save(NON_ANONYMOUS_REDIRECTION);

        Flowable<HttpResponse<String>> response =
                httpClient.exchange(HttpRequest.create(HttpMethod.GET, "/" + NON_ANONYMOUS_URL_ID), String.class);

        response.blockingFirst();

        verify(analyticsServiceClient).redirectionClicked(NON_ANONYMOUS_URL_ID);
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

    @MockBean(AnalyticsServiceClient.class)
    public AnalyticsServiceClient createMockListener() {
        return Mockito.mock(AnalyticsServiceClient.class);
    }
}