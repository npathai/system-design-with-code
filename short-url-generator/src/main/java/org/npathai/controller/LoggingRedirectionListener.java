package org.npathai.controller;

import io.micronaut.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoggingRedirectionListener implements RedirectionListener {
    private static final Logger LOG = LogManager.getLogger(LoggingRedirectionListener.class);

    @Override
    public void onSuccessfulRedirection(HttpRequest request, String id, String longUrl) {
        Optional<String> xForwardedFor = request.getHeaders().findFirst("X-Forwarded-For");
        LOG.info("X-Forwarded for: {}", xForwardedFor);
        LOG.info("All headers: {}", request.getHeaders().asMap());
        LOG.info("Redirection [{}] -> Request: [{}]", id, request.getHeaders());
    }
}
