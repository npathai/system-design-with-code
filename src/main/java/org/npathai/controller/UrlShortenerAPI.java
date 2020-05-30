package org.npathai.controller;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.npathai.domain.UrlShortener;
import org.npathai.model.ShortUrl;
import spark.Request;
import spark.Response;

public class UrlShortenerAPI {
    private final UrlShortener shortener;

    public UrlShortenerAPI(UrlShortener shortener) {
        this.shortener = shortener;
    }

    public String shorten(Request req, Response res) {
        ShortenUrlRequest shortenUrlRequest = formShortenRequest(req);
        ShortUrl shortUrl = shortener.shorten(shortenUrlRequest.longUrl());
        return prepareOkResponse(res, shortUrl);
    }

    private String prepareOkResponse(Response res, ShortUrl shortUrl) {
        res.type("application/json");
        return jsonFor(shortUrl);
    }

    private String jsonFor(ShortUrl shortUrl) {
        return new JsonObject()
                .add("id", shortUrl.id())
                .add("longUrl", shortUrl.longUrl())
                .add("createdAt", shortUrl.createdAt())
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
