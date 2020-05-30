package org.npathai.controller;

import org.npathai.domain.UrlShortener;
import spark.Request;
import spark.Response;

public class RootController {

    private final UrlShortener urlShortener;

    public RootController(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    public String handle(Request req, Response res) {
        String id = req.params("id");
        String longUrl = urlShortener.expand(id);
        if (longUrl == null) {
            return prepare404Response(res);
        }
        return prepareRedirectResponse(res, longUrl);
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
