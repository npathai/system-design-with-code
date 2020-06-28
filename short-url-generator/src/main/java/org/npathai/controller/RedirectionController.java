package org.npathai.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.domain.UrlShortener;

import java.net.URI;
import java.util.Optional;

@Controller
public class RedirectionController {
    private static final Logger LOG = LogManager.getLogger(RedirectionController.class);

    private final UrlShortener urlShortener;

    public RedirectionController(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    @Secured("isAnonymous()")
    @Get("/{id}")
    public HttpResponse<String> handle(String id) throws Exception {
        try {
            LOG.info("Request received for expanding id: " + id);
            Optional<String> redirection = urlShortener.expand(id);
            if (redirection.isEmpty()) {
                return HttpResponse.notFound();
            }
            LOG.info("Returning redirect response");
            return HttpResponse.redirect(URI.create(redirection.get()));
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }
}
