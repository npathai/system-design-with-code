package org.npathai.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("url-lifetime-in-secs")
public class UrlLifetimeConfiguration {

    private String anonymous;
    private String authenticated;

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(String authenticated) {
        this.authenticated = authenticated;
    }
}
