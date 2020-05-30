package org.npathai;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.npathai.dao.InMemoryUrlDao;
import org.npathai.domain.UrlShortener;
import org.npathai.model.ShortUrl;
import org.npathai.service.IdGenerationService;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    public static final String ID = "AAAAA";

    @Mock
    private IdGenerationService idGenerationService;

    private UrlShortener shortener;

    @Before
    public void initialize() {
        MockitoAnnotations.initMocks(this);
        shortener = new UrlShortener(idGenerationService, new InMemoryUrlDao());
        Mockito.when(idGenerationService.getId()).thenReturn(ID);
    }

    @Parameters(method = "longUrls")
    @Test
    public void validateShortenedUrlFormat(String longUrl) {
        ShortUrl shortUrl = shortener.shorten(longUrl);

        assertThat(shortUrl).isNotNull();
        Mockito.verify(idGenerationService).getId();
        assertThat(shortUrl.longUrl()).isEqualTo(longUrl);
        assertThat(shortUrl.id()).isEqualTo(ID);
    }

    @Parameters(method = "longUrls")
    @Test
    public void returnsOriginalUrlForAShortenedUrl(String originalLongUrl) {
        ShortUrl shortUrl = shortener.shorten(originalLongUrl);
        assertThat(shortener.expand(shortUrl.id())).isEqualTo(originalLongUrl);
    }

    @SuppressWarnings("unused")
    static Object[][] longUrls() {
        return new Object[][]{
                new Object[] {"http://google.com"},
                new Object[] {"http://youtube.com"}
        };
    }
}