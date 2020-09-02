package org.npathai.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.client.AnalyticsServiceClient;
import org.npathai.domain.UrlShortener;
import io.micrometer.core.instrument.MeterRegistry;
import org.npathai.metrics.ServiceTags;
import org.npathai.model.Redirection;

import java.net.URI;
import java.util.Optional;

@Controller
public class RedirectionController {
    private static final Logger LOG = LogManager.getLogger(RedirectionController.class);
    public static final String REDIRECTION_API_ENDPOINT = "/{id}";

    private final UrlShortener urlShortener;
    private final AnalyticsServiceClient analyticsServiceClient;
    private final MeterRegistry meterRegistry;

    public RedirectionController(UrlShortener urlShortener, AnalyticsServiceClient analyticsServiceClient,
                                 MeterRegistry meterRegistry) {
        this.urlShortener = urlShortener;
        this.analyticsServiceClient = analyticsServiceClient;
        this.meterRegistry = meterRegistry;
    }

    @Timed(value = "http.response.time", extraTags = {"short.url.generator", "redirect"})
    @Secured("isAnonymous()")
    @Get(REDIRECTION_API_ENDPOINT)
    public HttpResponse<String> handle(HttpRequest<?> httpRequest, String id) throws Exception {
        Tags commonTags = ServiceTags.httpApiTags("short.url.generator",  "redirect",
                REDIRECTION_API_ENDPOINT, HttpMethod.GET);

        try {
            meterRegistry.counter("http.requests.total", commonTags).increment();

            LOG.info("Request received for expanding id: " + id);

            Optional<Redirection> redirection = urlShortener.expand(id);
            if (redirection.isEmpty()) {
                meterRegistry.counter("http.responses.total",
                        ServiceTags.httpNotFoundStatusTags(commonTags)).increment();
                return HttpResponse.notFound();
            }

            if (!redirection.get().isAnonymous()) {
                analyticsServiceClient.redirectionClicked(id);
            }

            LOG.info("Returning redirect response");
            meterRegistry.counter("http.responses.total", ServiceTags.httpOkStatusTags(commonTags)).increment();
            return prepareRedirectResponse(redirection);
        } catch (Exception ex) {
            meterRegistry.counter("http.responses.total",
                    ServiceTags.httpInternalErrorStatusTags(commonTags)).increment();
            LOG.error(ex);
            throw ex;
        }
    }

    private MutableHttpResponse<String> prepareRedirectResponse(Optional<Redirection> redirection) {
        MutableHttpResponse<String> response = HttpResponse.redirect(URI.create(redirection.get().longUrl()));
        // We want to avoid caching due to statistics, we wan't request to be served from server every time
        response.header("Cache-Control.no-store");
        return response;
    }
}
