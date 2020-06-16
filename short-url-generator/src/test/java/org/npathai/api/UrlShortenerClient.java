package org.npathai.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;

@Client("/shorten")
public interface UrlShortenerClient {

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    String shorten(@Body ShortenRequest shortenRequest);
}
