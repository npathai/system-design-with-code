package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;


@Controller("/shorten")
public class UrlShortenerAPI {
    private final UrlShortener shortener;

    public UrlShortenerAPI(UrlShortener shortener) {
        this.shortener = shortener;
    }

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public String shorten(@Body ShortenRequest shortenRequest) throws Exception {
        Redirection redirection = shortener.shorten(shortenRequest.getLongUrl());
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
