package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import org.npathai.domain.IdGenerationService;
import spark.Request;
import spark.Response;

public class IdGeneratorAPI {

    private final IdGenerationService idGenerationService;

    public IdGeneratorAPI(IdGenerationService idGenerationService) {
        this.idGenerationService = idGenerationService;
    }

    public String generate(Request req, Response res) {
        return prepareOkResponse(res, idGenerationService.nextId());
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
