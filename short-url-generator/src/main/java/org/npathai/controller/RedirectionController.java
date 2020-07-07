package org.npathai.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.client.AnalyticsServiceClient;
import org.npathai.domain.UrlShortener;

import java.net.URI;
import java.util.Optional;

@Controller
public class RedirectionController {
    private static final Logger LOG = LogManager.getLogger(RedirectionController.class);

    private final UrlShortener urlShortener;
    private final AnalyticsServiceClient analyticsServiceClient;

    public RedirectionController(UrlShortener urlShortener, AnalyticsServiceClient analyticsServiceClient) {
        this.urlShortener = urlShortener;
        this.analyticsServiceClient = analyticsServiceClient;
    }

    @Secured("isAnonymous()")
    @Get("/{id}")
    public HttpResponse<String> handle(HttpRequest<?> httpRequest, String id) throws Exception {
        try {
            LOG.info("Request received for expanding id: " + id);
            Optional<String> redirection = urlShortener.expand(id);
            if (redirection.isEmpty()) {
                return HttpResponse.notFound();
            }

            // FIXME we should only be invoking analytics service for non-anonymous redirections.
            //  Need to find way to detect that info. Cannot use URLShortener.expand as we are not storing full
            // information like UserId in redis cache
            analyticsServiceClient.redirectionClicked(id);
            LOG.info("Returning redirect response");

            return prepareRedirectResponse(redirection);
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    private MutableHttpResponse<String> prepareRedirectResponse(Optional<String> redirection) {
        MutableHttpResponse<String> response = HttpResponse.redirect(URI.create(redirection.get()));
        // We want to avoid caching due to statistics, we wan't request to be served from server every time
        response.header("Cache-Control", "no-store");
        return response;
    }
}
