package org.npathai.api;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

@Client("/login")
public interface LoginClient {

    @Post("/")
    BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);
}
