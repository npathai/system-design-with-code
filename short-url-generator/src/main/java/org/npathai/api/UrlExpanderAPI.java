package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import org.npathai.domain.UrlShortener;
import org.npathai.model.Redirection;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class UrlExpanderAPI {

    private final UrlShortener urlShortener;

    public UrlExpanderAPI(UrlShortener urlShortener) {
        this.urlShortener = urlShortener;
    }

    public String expand(Request req, Response res) throws Exception {
        String id = req.params("id");
        Optional<Redirection> redirection = urlShortener.expand(id);
        if (redirection.isPresent()) {
            return prepareOkResponse(res, id, redirection.get().longUrl());
        } else {
            return prepareNotFoundResponse(res);
        }
    }

    private String prepareNotFoundResponse(Response res) {
        res.status(404);
        return null;
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
