package org.npathai.controller;

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

    @Secured("isAnonymous()")
    @Get(REDIRECTION_API_ENDPOINT)
    public HttpResponse<String> handle(HttpRequest<?> httpRequest, String id) throws Exception {
        Tags commonTags = ServiceTags.httpApiTags("short.url.generator",  "redirect",
                REDIRECTION_API_ENDPOINT, HttpMethod.GET);

        try {
            meterRegistry.counter("http.requests.total", commonTags).increment();

            LOG.info("Request received for expanding id: " + id);

            Optional<String> redirection = urlShortener.expand(id);
            if (redirection.isEmpty()) {
                meterRegistry.counter("http.responses.total",
                        ServiceTags.httpNotFoundStatusTags(commonTags)).increment();
                return HttpResponse.notFound();
            }

            // FIXME we should only be invoking analytics service for non-anonymous redirections.
            //  Need to find way to detect that info. Cannot use URLShortener.expand as we are not storing full
            // information like UserId in redis cache
            analyticsServiceClient.redirectionClicked(id);

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

    private MutableHttpResponse<String> prepareRedirectResponse(Optional<String> redirection) {
        MutableHttpResponse<String> response = HttpResponse.redirect(URI.create(redirection.get()));
        // We want to avoid caching due to statistics, we wan't request to be served from server every time
        response.header("Cache-Control.no-store");
        return response;
    }
}
