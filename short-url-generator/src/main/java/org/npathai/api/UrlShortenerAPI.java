package org.npathai.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;
import spark.Request;
import spark.Response;

public class UrlShortenerAPI {
    private final UrlShortener shortener;

    public UrlShortenerAPI(UrlShortener shortener) {
        this.shortener = shortener;
    }

    public String shorten(Request req, Response res) {
        ShortenUrlRequest shortenUrlRequest = formShortenRequest(req);
        Redirection redirection = null;
        try {
            redirection = shortener.shorten(shortenUrlRequest.longUrl());
            return prepareOkResponse(res, redirection);
        } catch (Exception e) {
            return prepareUnavailableResponse(res);
        }
    }

    private String prepareUnavailableResponse(Response res) {
        res.status(503);
        return null;
    }

    private String prepareOkResponse(Response res, Redirection redirection) {
        res.type("application/json");
        return jsonFor(redirection);
    }

    private String jsonFor(Redirection redirection) {
        return new JsonObject()
                .add("id", redirection.id())
                .add("longUrl", redirection.longUrl())
                .add("createdAt", redirection.createdAt())
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
