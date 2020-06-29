package org.npathai.controller;

import io.micronaut.http.HttpRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.domain.AnalyticsService;

public class AnalyticsRedirectionListener implements RedirectionListener {
    private static final Logger LOG = LogManager.getLogger(AnalyticsRedirectionListener.class);

    private final AnalyticsService analyticsService;

    public AnalyticsRedirectionListener(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public void onSuccessfulRedirection(HttpRequest request, String id, String longUrl) {
        analyticsService.onSuccessfulRedirection(id, longUrl, request.getHeaders().asMap());
    }
}
