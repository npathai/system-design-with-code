package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import org.npathai.service.IdProviderService;
import spark.Request;
import spark.Response;

public class IdGeneratorAPI {

    private final IdProviderService idProviderService;

    public IdGeneratorAPI(IdProviderService idProviderService) {
        this.idProviderService = idProviderService;
    }

    public String generate(Request req, Response res) {
        return prepareOkResponse(res, idProviderService.nextId());
    }

    private String prepareOkResponse(Response res, String generatedId) {
        res.type("application/json");
        res.status(201);
        return jsonFor(generatedId);
    }

    private String jsonFor(String generatedId) {
        return new JsonObject()
                .add("id", generatedId)
                .toString();
    }
}
