package org.npathai.client;

import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

@Client("analytics-service")
public interface AnalyticsServiceClient {

    @Post("/analytics/{id}")
    void redirectionCreated(String id);

    @Post("/analytics/{id}/click")
    void redirectionClicked(String id);
}
