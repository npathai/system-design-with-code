package org.npathai.controller;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.npathai.domain.UrlShortener;
import spark.Request;
import spark.Response;

public class UrlShortnerController {
    private final UrlShortener shortener;

    public UrlShortnerController(UrlShortener shortener) {
        this.shortener = shortener;
    }

    public String shorten(Request req, Response res) {
        ShortenUrlRequest shortenUrlRequest = formShortenRequest(req);
        String shortUrlId = shortener.shorten(shortenUrlRequest.longUrl());
        return prepareOkResponse(shortenUrlRequest, res, shortUrlId);
    }

    private String prepareOkResponse(ShortenUrlRequest shortenUrlRequest, Response res, String shortUrl) {
        res.type("application/json");
        return jsonFor(shortenUrlRequest, shortUrl);
    }

    private String jsonFor(ShortenUrlRequest shortenUrlRequest, String shortUrl) {
        return new JsonObject()
                .add("id", shortUrl)
                .add("longUrl", shortenUrlRequest.longUrl())
                .toString();
    }

    private ShortenUrlRequest formShortenRequest(Request req) {
        return new ShortenUrlRequest(req);
    }

    private class ShortenUrlRequest {

        private final Request req;
        private final JsonObject parsedReq;

        public ShortenUrlRequest(Request req) {
            this.req = req;
            this.parsedReq = Json.parse(req.body()).asObject();
        }

        public String longUrl() {
            return parsedReq.get("longUrl").asString();
        }
    }
}
