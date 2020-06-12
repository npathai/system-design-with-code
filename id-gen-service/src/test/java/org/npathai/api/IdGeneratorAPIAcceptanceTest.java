package org.npathai.api;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import io.restassured.response.Response;
import org.junit.Test;
import org.npathai.AcceptanceTests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class IdGeneratorAPIAcceptanceTest {

    @Test
    public void idFormatTest() {
        String url = AcceptanceTests.BASEURL + "/generate";
        Response response = given()
                .when()
                .get(url);

        JsonObject jsonObject = Json.parse(response.getBody().asString()).asObject();
        String id = jsonObject.get("id").asString();
        assertThat(id).hasSize(5);
    }


}
