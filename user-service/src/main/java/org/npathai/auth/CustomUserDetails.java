package org.npathai.auth;

import io.micronaut.security.authentication.UserDetails;

import java.util.Collection;
import java.util.Map;

public class CustomUserDetails extends UserDetails {
    private String userId;

    public CustomUserDetails(String username, Collection<String> roles, String userId) {
        super(username, roles);
        this.userId = userId;
    }

    public CustomUserDetails(String username, Collection<String> roles, Map<String, Object> attributes,
                             String userId) {
        super(username, roles, attributes);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
