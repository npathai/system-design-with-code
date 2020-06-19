package org.npathai;

import io.micronaut.http.client.annotation.Client;

@Client(id = "user-service")
public interface LoginClient extends AuthenticationOperations {

}
