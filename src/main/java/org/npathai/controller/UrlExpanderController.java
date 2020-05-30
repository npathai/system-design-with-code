package org.npathai.controller;

import com.eclipsesource.json.JsonObject;
import org.npathai.domain.UrlShortener;
import spark.Request;
import spark.Response;

public class UrlExpanderController {

    private final UrlShortener urlShortener;

    public UrlExpanderController(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    public String expand(Request req, Response res) {
        String id = req.params("id");
        String longUrl = urlShortener.toLong(id);
        return prepareOkResponse(res, id, longUrl);
    }

    private String prepareOkResponse(Response res, String id, String longUrl) {
        res.type("application/json");
        return jsonFor(id, longUrl);
    }

    private String jsonFor(String id, String longUrl) {
        return new JsonObject()
                .add("id", id)
                .add("longUrl", longUrl)
                .toString();
    }
}
