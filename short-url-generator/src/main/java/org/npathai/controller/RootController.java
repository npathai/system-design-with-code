package org.npathai.controller;

import org.npathai.domain.UrlShortener;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class RootController {

    private final UrlShortener urlShortener;

    public RootController(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    public String handle(Request req, Response res) throws Exception {
        String id = req.params("id");
        Optional<String> redirection = urlShortener.expand(id);
        if (redirection.isEmpty()) {
            return prepare404Response(res);
        }
        return prepareRedirectResponse(res, redirection.get());
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
