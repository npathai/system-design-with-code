package org.npathai.discourse.application.users.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.npathai.discourse.application.domain.users.RegistrationData;

@Client("/users")
public interface UserControllerClient {

    @Post
    HttpResponse<Void> create(RegistrationData registrationData);
}
