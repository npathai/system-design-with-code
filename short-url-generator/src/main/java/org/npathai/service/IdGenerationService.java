package org.npathai.service;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class IdGenerationService {
    public static final String SERVICE_URL = "http://localhost:4322/generate";

    private Client client = ClientBuilder.newClient();

    public String getId() {
        try {
            String response = client.target(SERVICE_URL)
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);

            JsonObject responseJson = Json.parse(response).asObject();

            return responseJson.get("id").asString();
        } catch (ProcessingException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
