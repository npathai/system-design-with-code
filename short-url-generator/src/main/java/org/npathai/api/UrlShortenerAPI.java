package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import org.npathai.ShortenRequest;
import org.npathai.UrlShortenerOperations;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;

import javax.annotation.Nullable;
import java.security.Principal;

@Controller
public class UrlShortenerAPI implements UrlShortenerOperations {
    private final UrlShortener shortener;

    public UrlShortenerAPI(UrlShortener shortener) {
        this.shortener = shortener;
    }

    @Override
    public String shorten(@Nullable Principal principal, @Body ShortenRequest shortenRequest) throws Exception {
        shortenRequest.setPrincipal(principal);
        Redirection redirection = shortener.shorten(shortenRequest);
        return jsonFor(redirection);
    }

    private String jsonFor(Redirection redirection) {
        return new JsonObject()
                .add("id", redirection.id())
                .add("longUrl", redirection.longUrl())
                .add("createdAt", redirection.createdAt())
                .toString();
    }
}
