package org.npathai.controller;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.npathai.AcceptanceTests;

import static io.restassured.RestAssured.given;

public class UrlShortnerControllerAcceptanceTest {

    @Test
    public void returnsShortenedUrl() {
        String url = AcceptanceTests.BASEURL + "/shorten";
        System.out.println(url);
        Response response = given()
                .body(withJsonContaining("http://google.com"))
                .when()
                .post(url);

        JsonObject jsonObject = Json.parse(response.getBody().asString()).asObject();
        String id = jsonObject.get("id").asString();
        Assertions.assertThat(id).hasSize(5);
        String longUrl = jsonObject.get("longUrl").asString();
        Assertions.assertThat(longUrl).isEqualTo("http://google.com");
    }

    @Test
    public void returnsExpandedUrlForId() {
        String shortenUrl = AcceptanceTests.BASEURL + "/shorten";
        Response shortenedResponse = given()
                .body(withJsonContaining("http://google.com"))
                .when()
                .post(shortenUrl);

        JsonObject jsonObject = Json.parse(shortenedResponse.getBody().asString()).asObject();
        String id = jsonObject.get("id").asString();

        String url = AcceptanceTests.BASEURL + "/expand/" + id;
        Response response = RestAssured.when()
                .get(url);

        JsonObject jsonObject1 = Json.parse(response.getBody().asString()).asObject();
        String longUrl = jsonObject1.get("longUrl").asString();
        String responseId = jsonObject.get("id").asString();
        Assertions.assertThat(responseId).isEqualTo(id);
        Assertions.assertThat(longUrl).isEqualTo("http://google.com");
    }

    private String withJsonContaining(String longUrl) {
        return new JsonObject()
                .add("longUrl", longUrl)
                .toString();
    }

}