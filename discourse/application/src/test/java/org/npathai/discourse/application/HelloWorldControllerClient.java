package org.npathai.discourse.application;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("/")
public interface HelloWorldControllerClient {

    @Get("/hello")
    String helloWorld();
}
