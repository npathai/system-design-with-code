package org.npathai.api;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlShortenerAPIAcceptanceTest {

//    public static final String LONG_URL = "http://google.com";
//
//    @Test
//    public void returnsShortenedUrl() {
//        String url = AcceptanceTests.BASEURL + "/shorten";
//        Response response = given()
//                .body(withJsonContaining(LONG_URL))
//                .when()
//                .post(url);
//
//        JsonObject jsonObject = Json.parse(response.getBody().asString()).asObject();
//        String id = jsonObject.get("id").asString();
//        assertThat(id).hasSize(5);
//
//        String longUrl = jsonObject.get("longUrl").asString();
//        assertThat(longUrl).isEqualTo(LONG_URL);
//
//        long createdAt = jsonObject.get("createdAt").asLong();
//        assertThat(createdAt).isCloseTo(System.currentTimeMillis(), Offset.offset(1000L));
//    }
//
//    @Test
//    public void returnsExpandedUrlForId() {
//        String shortenUrl = AcceptanceTests.BASEURL + "/shorten";
//        Response shortenedResponse = given()
//                .body(withJsonContaining(LONG_URL))
//                .when()
//                .post(shortenUrl);
//
//        JsonObject jsonObject = Json.parse(shortenedResponse.getBody().asString()).asObject();
//        String id = jsonObject.get("id").asString();
//
//        String url = AcceptanceTests.BASEURL + "/expand/" + id;
//        Response response = RestAssured.when()
//                .get(url);
//
//        JsonObject jsonObject1 = Json.parse(response.getBody().asString()).asObject();
//        String longUrl = jsonObject1.get("longUrl").asString();
//        String responseId = jsonObject.get("id").asString();
//        assertThat(responseId).isEqualTo(id);
//        assertThat(longUrl).isEqualTo(LONG_URL);
//    }
//
//    private String withJsonContaining(String longUrl) {
//        return new JsonObject()
//                .add("longUrl", longUrl)
//                .toString();
//    }

}