package org.npathai;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.npathai.dao.InMemoryUrlDao;
import org.npathai.domain.UrlShortener;
import org.npathai.model.ShortUrl;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    private UrlShortener shortener;

    @Before
    public void initialize() {
        shortener = new UrlShortener(new InMemoryUrlDao());
    }

    @Test
    public void validateShortenedUrlFormat() {
        ShortUrl shortUrl = shortener.shorten("http://google.com");
        assertThat(shortUrl).isNotNull();
        assertThat(shortUrl.id()).hasSize(5);
    }

    @Parameters(method = "longUrls")
    @Test
    public void returnsOriginalUrlForAShortenedUrl(String originalLongUrl) {
        ShortUrl shortUrl = shortener.shorten(originalLongUrl);
        assertThat(shortener.expand(shortUrl.id())).isEqualTo(originalLongUrl);
    }

    @Test
    public void returnsUniqueUrl() {
        Set<ShortUrl> shortUrls = new LinkedHashSet<>();
        for (int i = 0; i < 1000; i++) {
            ShortUrl s = shortener.shorten(LONG_URL);
            shortUrls.add(s);
        }

        assertThat(shortUrls).hasSize(1000);
    }

    @SuppressWarnings("unused")
    static Object[][] longUrls() {
        return new Object[][]{
                new Object[] {"http://google.com"},
                new Object[] {"http://youtube.com"}
        };
    }
}