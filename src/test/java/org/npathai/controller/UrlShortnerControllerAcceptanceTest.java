package org.npathai.controller;

import com.eclipsesource.json.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.npathai.AcceptanceTests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class UrlShortnerControllerAcceptanceTest {

    @Test
    public void returnsShortenedUrl() {
        String url = AcceptanceTests.BASEURL + "/shorten";
        System.out.println(url);
        ValidatableResponse shortUrl = given()
                .body(withJsonContaining("http://google.com"))
                .when()
                .post(url)
                .then()
                .contentType(ContentType.JSON)
                .body("shortUrl", Matchers.startsWith("http://localhost/"));
    }

    private String withJsonContaining(String longUrl) {
        return new JsonObject()
                .add("longUrl", longUrl)
                .toString();
    }

}