package org.npathai.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.npathai.api.ShortenRequest;
import org.npathai.cache.RedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.util.time.MutableClock;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    public static final String ID = "AAAAA";

    private Properties applicationProperties = new Properties();

    @Mock
    private IdGenerationServiceClient idGenerationServiceClient;
    private MutableClock mutableClock = new MutableClock();

    private UrlShortener shortener;
    private RedirectionCache redirectionCache;
    private InMemoryRedirectionDao inMemoryRedirectionDao;

    @BeforeEach
    public void initialize() throws Exception {
        MockitoAnnotations.initMocks(this);
        redirectionCache = Mockito.mock(RedirectionCache.class);
        when(redirectionCache.getById(anyString())).thenReturn(Optional.empty());
        inMemoryRedirectionDao = spy(new InMemoryRedirectionDao());
        applicationProperties.put("anonymousUrlLifetimeInSeconds", "60");
        shortener = new UrlShortener(applicationProperties, idGenerationServiceClient, inMemoryRedirectionDao,
                redirectionCache, mutableClock);
        when(idGenerationServiceClient.generateId()).thenReturn(ID);
    }

    @MethodSource("longUrls")
    @ParameterizedTest
    public void validateShortenedUrlFormat(String longUrl) throws Exception {
        Redirection redirection = shortenAnonymously(longUrl);

        assertThat(redirection).isNotNull();
        Mockito.verify(idGenerationServiceClient).generateId();
        assertThat(redirection.longUrl()).isEqualTo(longUrl);
        assertThat(redirection.id()).isEqualTo(ID);
    }

    @MethodSource("longUrls")
    @ParameterizedTest
    public void returnsOriginalUrlForAShortenedUrl(String originalLongUrl) throws Exception {
        Redirection redirection = shortenAnonymously(originalLongUrl);
        assertThat(shortener.expand(redirection.id()).get()).isEqualTo(originalLongUrl);
    }

    @SuppressWarnings("unused")
    static List<Arguments> longUrls() {
        return List.of(
                Arguments.of("http://google.com"),
                Arguments.of("http://youtube.com")
        );
    }

    @Test
    public void putsValueInCacheForFastAccess() throws Exception {
        Redirection redirection = shortenAnonymously(LONG_URL);

        shortener.expand(redirection.id());

        verify(redirectionCache).put(redirection);
    }

    @Test
    public void returnsValueFromFastCacheOnSubsequentCalls() throws Exception {
        Redirection redirection = shortenAnonymously(LONG_URL);
        reset(inMemoryRedirectionDao);
        when(redirectionCache.getById(redirection.id())).thenReturn(Optional.of(redirection));

        Optional<String> longUrl = shortener.expand(redirection.id());

        assertThat(longUrl).hasValue(LONG_URL);
        verify(redirectionCache).getById(redirection.id());
        verifyZeroInteractions(inMemoryRedirectionDao);
    }

    @Nested
    class AnonymousUser {

        @ParameterizedTest
        @CsvSource({
                "60",
                "100",
                "1000"
        })
        public void redirectionExpiresAfterConfiguredDuration(int expiryInSeconds) throws Exception {
            applicationProperties.put("anonymousUrlLifetimeInSeconds",
                    String.valueOf(expiryInSeconds));

            Redirection redirection = shortenAnonymously(LONG_URL);
            long expiryTime = redirection.expiryTimeInMillis();

            assertThat(expiryTime - redirection.createdAt()).isEqualTo(Duration.ofSeconds(expiryInSeconds).toMillis());
        }
    }

    @Nested
    class AuthenticatedUser {

        @ParameterizedTest
        @CsvSource({
                "60",
                "100",
                "1000"
        })
        public void redirectionExpiresAfterConfiguredDuration(int expiryInSeconds) throws Exception {
            applicationProperties.put("authenticatedUserUrlLifetimeInSeconds",
                    String.valueOf(expiryInSeconds));

            Redirection redirection = shortenAuthenticated(LONG_URL);

            long expiryTime = redirection.expiryTimeInMillis();

            assertThat(expiryTime - redirection.createdAt()).isEqualTo(Duration.ofSeconds(expiryInSeconds).toMillis());
        }
    }

    @ParameterizedTest
    @MethodSource("durationsGreaterThanOrEqualToOneMinute")
    public void deletesExpiredUrlsFromDatabaseLazilyWhenRequestedForFirstTime(Duration duration) throws Exception {
        Redirection redirection = shortenAnonymously(LONG_URL);

        mutableClock.advanceBy(duration);

        shortener.expand(redirection.id());

        verify(inMemoryRedirectionDao).deleteById(redirection.id());
    }

    @ParameterizedTest
    @MethodSource("durationsGreaterThanOrEqualToOneMinute")
    public void deletesExpiredUrlsFromCacheAndDatabaseLazilyWhenRequested(Duration duration) throws Exception {
        Redirection redirection = shortenAnonymously(LONG_URL);
        when(redirectionCache.getById(redirection.id())).thenReturn(Optional.of(redirection));

        mutableClock.advanceBy(duration);

        shortener.expand(redirection.id());

        verify(redirectionCache).deleteById(redirection.id());
        verify(inMemoryRedirectionDao).deleteById(redirection.id());
    }

    static List<Arguments> durationsGreaterThanOrEqualToOneMinute() {
        return List.of(
                Arguments.of(Duration.ofMinutes(1)),
                Arguments.of(Duration.ofMinutes(2)),
                Arguments.of(Duration.ofHours(1)),
                Arguments.of(Duration.ofDays(1))
        );
    }

    @ParameterizedTest
    @MethodSource("durationsGreaterThanOrEqualToOneMinute")
    public void returnsEmptyRedirectionWhenItIsExpired(Duration duration) throws Exception {
        Redirection redirection = shortenAnonymously(LONG_URL);
        when(redirectionCache.getById(redirection.id())).thenReturn(Optional.of(redirection));

        mutableClock.advanceBy(duration);

        assertThat(shortener.expand(redirection.id())).isEmpty();
    }

    @Test
    public void returnsEmptyRedirectionForUnknownId() throws Exception {
        assertThat(shortener.expand("GFSFA")).isEmpty();
    }

    private Redirection shortenAnonymously(String longUrl) throws Exception {
        ShortenRequest shortenRequest = new ShortenRequest();
        shortenRequest.setLongUrl(longUrl);
        return shortener.shorten(shortenRequest);
    }

    private Redirection shortenAuthenticated(String longUrl) throws Exception {
        ShortenRequest shortenRequest = new ShortenRequest();
        shortenRequest.setLongUrl(longUrl);
        shortenRequest.setPrincipal(Mockito.mock(Principal.class));
        return shortener.shorten(shortenRequest);
    }


}