package org.npathai;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.npathai.domain.UrlShortener;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    private UrlShortener shortener;

    @Before
    public void initialize() {
        shortener = new UrlShortener();
    }

    @Test
    public void validateShortenedUrlFormat() {
        String shortUrl = shortener.toShort("http://google.com");
        assertThat(shortUrl).isNotNull()
                .startsWith("http://localhost/");
        String key = shortUrl.substring(shortUrl.lastIndexOf("/") + 1);
        assertThat(key).hasSize(5);
    }

    @MethodSource("longUrls")
    @ParameterizedTest
    public void returnsOriginalUrlForAShortenedUrl(String originalLongUrl) {
        String shortUrl = shortener.toShort(originalLongUrl);
        assertThat(shortener.toLong(shortUrl)).isEqualTo(originalLongUrl);
    }

    @Test
    public void returnsUniqueUrl() {
        Set<String> shortUrls = new LinkedHashSet<>();
        for (int i = 0; i < 1000; i++) {
            String s = shortener.toShort(LONG_URL);
            shortUrls.add(s);
        }

        System.out.println(shortUrls);
        assertThat(shortUrls).hasSize(1000);
    }

    static List<Arguments> longUrls() {
        return List.of(
                Arguments.of("http://google.com"),
                Arguments.of("http://youtube.com")
        );
    }
}