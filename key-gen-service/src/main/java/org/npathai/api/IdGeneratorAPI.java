package org.npathai.api;

import com.eclipsesource.json.JsonObject;
import org.npathai.domain.IdGenerator;
import spark.Request;
import spark.Response;

public class IdGeneratorAPI {

    private final IdGenerator idGenerator;

    public IdGeneratorAPI(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public String generate(Request req, Response res) {
        return prepareOkResponse(res, idGenerator.generate());
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
