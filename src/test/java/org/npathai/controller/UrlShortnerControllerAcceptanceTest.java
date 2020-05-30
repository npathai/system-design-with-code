package org.npathai.controller;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.npathai.AcceptanceTests;
import org.npathai.domain.UrlShortener;

import static org.hamcrest.Matchers.equalTo;

public class UrlShortnerControllerAcceptanceTest {

    private UrlShortnerController controller;

    @Before
    public void initialize() {
        controller = new UrlShortnerController(new UrlShortener());
    }

    @Test
    public void returnsShortenedUrl() {
        String url = AcceptanceTests.BASEURL + "/shorten";
        System.out.println(url);
        RestAssured.when().post()
                .then()
                    .body(equalTo("Hello World!"));
    }

}