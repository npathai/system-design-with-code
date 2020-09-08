package org.npathai.model;

import io.micronaut.security.authentication.Authentication;

public class UserInfo {
    private final String uid;
    private final String username;

    public UserInfo(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public static UserInfo fromAuthentication(Authentication authentication) {
        UserInfo userInfo = new UserInfo(
                authentication.getAttributes().get("uid").toString(),
                authentication.getName());
        return userInfo;
    }

    public String uid() {
        return uid;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
