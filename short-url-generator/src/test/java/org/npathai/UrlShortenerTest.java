package org.npathai;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.npathai.cache.RedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;
import org.npathai.util.time.MutableClock;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(JUnitParamsRunner.class)
public class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    public static final String ID = "AAAAA";

    @Mock
    private IdGenerationServiceClient idGenerationServiceClient;
    private MutableClock mutableClock = new MutableClock();

    private UrlShortener shortener;
    private RedirectionCache redirectionCache;
    private InMemoryRedirectionDao inMemoryRedirectionDao;

    @Before
    public void initialize() throws Exception {
        MockitoAnnotations.initMocks(this);
        redirectionCache = Mockito.mock(RedirectionCache.class);
        when(redirectionCache.getById(anyString())).thenReturn(Optional.empty());
        inMemoryRedirectionDao = spy(new InMemoryRedirectionDao());

        shortener = new UrlShortener(idGenerationServiceClient, inMemoryRedirectionDao,
                redirectionCache, mutableClock);
        when(idGenerationServiceClient.getId()).thenReturn(ID);
    }

    @Parameters(method = "longUrls")
    @Test
    public void validateShortenedUrlFormat(String longUrl) throws Exception {
        Redirection redirection = shortener.shorten(longUrl);

        assertThat(redirection).isNotNull();
        Mockito.verify(idGenerationServiceClient).getId();
        assertThat(redirection.longUrl()).isEqualTo(longUrl);
        assertThat(redirection.id()).isEqualTo(ID);
    }

    @Parameters(method = "longUrls")
    @Test
    public void returnsOriginalUrlForAShortenedUrl(String originalLongUrl) throws Exception {
        Redirection redirection = shortener.shorten(originalLongUrl);
        assertThat(shortener.expand(redirection.id()).get()).isEqualTo(originalLongUrl);
    }

    @SuppressWarnings("unused")
    static Object[][] longUrls() {
        return new Object[][]{
                new Object[] {"http://google.com"},
                new Object[] {"http://youtube.com"}
        };
    }

    @Test
    public void putsValueInCacheForFastAccess() throws Exception {
        Redirection redirection = shortener.shorten(LONG_URL);

        shortener.expand(redirection.id());

        verify(redirectionCache).put(redirection);
    }

    @Test
    public void returnsValueFromFastCacheOnSubsequentCalls() throws Exception {
        Redirection redirection = shortener.shorten(LONG_URL);
        reset(inMemoryRedirectionDao);
        when(redirectionCache.getById(redirection.id())).thenReturn(Optional.of(redirection));

        Optional<String> longUrl = shortener.expand(redirection.id());

        assertThat(longUrl).hasValue(LONG_URL);
        verify(redirectionCache).getById(redirection.id());
        verifyZeroInteractions(inMemoryRedirectionDao);
    }

    @Test
    public void redirectionHaveExpiryOfOneMinute() throws Exception {
        Redirection redirection = shortener.shorten(LONG_URL);
        long expiryTime = redirection.expiryTimeInMillis();
        assertThat(expiryTime - redirection.createdAt()).isEqualTo(Duration.ofMinutes(1).toMillis());
    }

    @Test
    @Parameters(method = "durationsGreaterThanOrEqualToOneMinute")
    public void deletesExpiredUrlsFromDatabaseLazilyWhenRequestedForFirstTime(Duration duration) throws Exception {
        Redirection redirection = shortener.shorten(LONG_URL);

        mutableClock.advanceBy(duration);

        shortener.expand(redirection.id());

        verify(inMemoryRedirectionDao).deleteById(redirection.id());
    }

    @Test
    @Parameters(method = "durationsGreaterThanOrEqualToOneMinute")
    public void deletesExpiredUrlsFromCacheAndDatabaseLazilyWhenRequested(Duration duration) throws Exception {
        Redirection redirection = shortener.shorten(LONG_URL);
        when(redirectionCache.getById(redirection.id())).thenReturn(Optional.of(redirection));

        mutableClock.advanceBy(duration);

        shortener.expand(redirection.id());

        verify(redirectionCache).deleteById(redirection.id());
        verify(inMemoryRedirectionDao).deleteById(redirection.id());
    }

    public Object[][] durationsGreaterThanOrEqualToOneMinute() {
        return new Object[][] {
                new Object[] {Duration.ofMinutes(1)},
                new Object[] {Duration.ofMinutes(2)},
                new Object[] {Duration.ofHours(1)},
                new Object[] {Duration.ofDays(1)},
        };
    }

    @Test
    @Parameters(method = "durationsGreaterThanOrEqualToOneMinute")
    public void returnsEmptyRedirectionWhenItIsExpired(Duration duration) throws Exception {
        Redirection redirection = shortener.shorten(LONG_URL);
        when(redirectionCache.getById(redirection.id())).thenReturn(Optional.of(redirection));

        mutableClock.advanceBy(duration);

        assertThat(shortener.expand(redirection.id())).isEmpty();
    }

    @Test
    public void returnsEmptyRedirectionForUnknownId() throws Exception {
        assertThat(shortener.expand("GFSFA")).isEmpty();
    }
}