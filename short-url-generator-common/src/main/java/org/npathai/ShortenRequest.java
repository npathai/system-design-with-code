package org.npathai;

import java.security.Principal;

public class ShortenRequest {
    private String longUrl;
    private Principal principal;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public boolean isAnonymous() {
        return principal == null;
    }

    @Override
    public String toString() {
        return "ShortenRequest{" +
                "longUrl='" + longUrl + '\'' +
                ", principal=" + principal +
                '}';
    }
}