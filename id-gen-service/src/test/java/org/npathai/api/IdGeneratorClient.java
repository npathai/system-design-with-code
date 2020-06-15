package org.npathai.api;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("/")
public interface IdGeneratorClient {

    @Get("/generate")
    String generate();
}
