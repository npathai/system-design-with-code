package org.npathai.client;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;

@Client(id = "id-gen-service")
public interface IdGenerationServiceClient {

    @Get("/generate")
    @Produces(MediaType.TEXT_PLAIN)
    String generateId();
}
