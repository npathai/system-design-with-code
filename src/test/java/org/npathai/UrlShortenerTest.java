package org.npathai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    private UrlShortener shortener;

    @BeforeEach
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
        Set<String> shortUrls = IntStream.range(0, 1000)
                .mapToObj((i) -> shortener.toShort(LONG_URL))
                .collect(Collectors.toSet());

        assertThat(shortUrls).hasSize(1000);
    }

    static List<Arguments> longUrls() {
        return List.of(
                Arguments.of("http://google.com"),
                Arguments.of("http://youtube.com")
        );
    }
}