package org.npathai.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.domain.UrlShortener;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class RedirectionController {
    private static final Logger LOG = LogManager.getLogger(RedirectionController.class);

    private final UrlShortener urlShortener;

    public RedirectionController(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    public String handle(Request req, Response res) throws Exception {
        try {
            String id = req.params("id");
            LOG.info("Request received for expanding id: " + id);
            Optional<String> redirection = urlShortener.expand(id);
            if (redirection.isEmpty()) {
                return prepare404Response(res);
            }
            return prepareRedirectResponse(res, redirection.get());
        } catch (Exception ex) {
            LOG.error(ex);
            throw ex;
        }
    }

    private String prepareRedirectResponse(Response res, String longUrl) {
        res.redirect(longUrl, 301);
        return null;
    }

    private String prepare404Response(Response res) {
        res.status(404);
        return null;
    }
}
