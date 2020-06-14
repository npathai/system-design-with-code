package org.npathai.config;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("redis")
public class RedisConfiguration {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
