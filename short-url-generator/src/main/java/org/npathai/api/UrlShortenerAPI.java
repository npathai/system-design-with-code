package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import javax.annotation.Nullable;


@Controller("/shorten")
public class UrlShortenerAPI {
    private static final Logger LOG = LogManager.getLogger(UrlShortenerAPI.class);

    private final UrlShortener shortener;
    private final MeterRegistry metricRegistry;

    public UrlShortenerAPI(UrlShortener shortener, MeterRegistry metricRegistry) {
        this.shortener = shortener;
        this.metricRegistry = metricRegistry;
    }

    @Secured("isAnonymous()")
    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public String shorten(@Nullable Authentication principal,
                          @Body ShortenRequest shortenRequest) throws Exception {

        metricRegistry.counter("web.access.controller.url.shorten.request").increment();
        if (principal != null) {
            UserInfo userInfo = UserInfo.fromAuthentication(principal);
            shortenRequest.setUserInfo(userInfo);
        }
        Redirection redirection = shortener.shorten(shortenRequest);

        return jsonFor(redirection);
    }

    private String jsonFor(Redirection redirection) {
        return new JsonObject()
                .add("id", redirection.id())
                .add("longUrl", redirection.longUrl())
                .add("createdAt", redirection.createdAtMillis())
                .add("expiryAt", redirection.expiryAtMillis())
                .toString();
    }
}
