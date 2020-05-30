package org.npathai.model;

import java.util.Objects;

public class ShortUrl {
    private String id;
    private String longUrl;
    private long createdAt;

    public ShortUrl(String id, String longUrl) {
        this.id = id;
        this.longUrl = longUrl;
        createdAt = System.currentTimeMillis();
    }

    public String id() {
        return id;
    }

    public String longUrl() {
        return longUrl;
    }

    public long createdAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortUrl shortUrl = (ShortUrl) o;
        return Objects.equals(id, shortUrl.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
