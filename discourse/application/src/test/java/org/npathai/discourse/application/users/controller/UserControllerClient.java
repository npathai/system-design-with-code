package org.npathai.discourse.application.users.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.npathai.discourse.application.domain.users.RegistrationData;
import org.npathai.discourse.application.domain.users.User;

@Client(value = "/users")
public interface UserControllerClient {

    @Post
    HttpResponse<User> create(RegistrationData registrationData);
}
