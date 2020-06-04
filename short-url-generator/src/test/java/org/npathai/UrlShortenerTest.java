package org.npathai;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.npathai.dao.InMemoryRedirectionDao;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;
import org.npathai.client.IdGenerationServiceClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class UrlShortenerTest {

    public static final String LONG_URL = "http://google.com";
    public static final String ID = "AAAAA";

    @Mock
    private IdGenerationServiceClient idGenerationServiceClient;

    private UrlShortener shortener;

    @Before
    public void initialize() throws Exception {
        MockitoAnnotations.initMocks(this);
        shortener = new UrlShortener(idGenerationServiceClient, new InMemoryRedirectionDao());
        Mockito.when(idGenerationServiceClient.getId()).thenReturn(ID);
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
        assertThat(shortener.expand(redirection.id())).isEqualTo(originalLongUrl);
    }

    @SuppressWarnings("unused")
    static Object[][] longUrls() {
        return new Object[][]{
                new Object[] {"http://google.com"},
                new Object[] {"http://youtube.com"}
        };
    }
}