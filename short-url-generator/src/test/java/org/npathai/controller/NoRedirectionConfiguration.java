package org.npathai.controller;

import io.micronaut.http.client.DefaultHttpClientConfiguration;

import javax.inject.Singleton;

@Singleton
public class NoRedirectionConfiguration extends DefaultHttpClientConfiguration {
        @Override
        public boolean isFollowRedirects() {
            return false;
        }
    }