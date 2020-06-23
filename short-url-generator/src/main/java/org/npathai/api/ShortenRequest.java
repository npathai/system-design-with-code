package org.npathai.api;

import org.npathai.model.UserInfo;

import javax.annotation.Nullable;

public class ShortenRequest {
    private String longUrl;
    private UserInfo userInfo;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    @Nullable
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isAnonymous() {
        return userInfo == null;
    }
}
