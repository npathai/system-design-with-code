package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
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
import org.npathai.metrics.ServiceTags;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import javax.annotation.Nullable;


@Controller(UrlShortenerAPI.SHORTEN_API_ENDPOINT)
public class UrlShortenerAPI {
    private static final Logger LOG = LogManager.getLogger(UrlShortenerAPI.class);
    public static final String SHORTEN_API_ENDPOINT = "/shorten";

    private final UrlShortener shortener;
    private final MeterRegistry meterRegistry;

    public UrlShortenerAPI(UrlShortener shortener, MeterRegistry metricRegistry) {
        this.shortener = shortener;
        this.meterRegistry = metricRegistry;
    }

    @Timed(value = "http.response.time", extraTags = {"short.url.generator", "shortening"})
    @Secured("isAnonymous()")
    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public String shorten(@Nullable Authentication principal,
                          @Body ShortenRequest shortenRequest) throws Exception {

        Tags commonTags = ServiceTags.httpApiTags("short.url.generator",  "shortening",
                SHORTEN_API_ENDPOINT, HttpMethod.POST);

        if (principal != null) {
            UserInfo userInfo = UserInfo.fromAuthentication(principal);
            shortenRequest.setUserInfo(userInfo);
        }

        meterRegistry.counter("http.requests.total", commonTags).increment();

        Redirection redirection = shortener.shorten(shortenRequest);

        meterRegistry.counter("http.responses.total", ServiceTags.httpOkStatusTags(commonTags)).increment();

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
