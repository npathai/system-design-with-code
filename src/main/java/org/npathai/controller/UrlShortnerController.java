package org.npathai.controller;

import org.npathai.domain.UrlShortener;
import spark.Request;
import spark.Response;

public class UrlShortnerController {

    private final UrlShortener shortener;

    public UrlShortnerController(UrlShortener shortener) {
        this.shortener = shortener;
    }

    public String shorten(Request req, Response res) {
        return "Hello world!";
    }
}
