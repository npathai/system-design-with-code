package org.npathai.controller;

import io.micronaut.http.HttpRequest;

public interface RedirectionListener {
    void onSuccessfulRedirection(HttpRequest request, String id, String longUrl);
}
