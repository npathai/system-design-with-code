package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;

import java.util.Optional;

@Controller("/expand")
public class UrlExpanderAPI {

    private final UrlShortener urlShortener;

    public UrlExpanderAPI(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    @Get("/{id}")
    public HttpResponse<String> expand(String id) throws Exception {
        Optional<Redirection> redirection = urlShortener.getById(id);
        if (redirection.isPresent()) {
            return HttpResponse.ok(jsonFor(id, redirection.get().longUrl()));
        } else {
            return HttpResponse.notFound();
        }
    }

    private String jsonFor(String id, String longUrl) {
        return new JsonObject()
                .add("id", id)
                .add("longUrl", longUrl)
                .toString();
    }
}
