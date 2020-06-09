package org.npathai.model;

import java.util.Objects;

public class Redirection {
    private String id;
    private final String longUrl;
    private long createdAt;

    public Redirection(String id, String longUrl) {
        this.id = id;
        this.longUrl = longUrl;
        // TODO not sure if should assign it here or should be inserted by DB layer.
        createdAt = System.currentTimeMillis();
    }

    public Redirection(String id, String lonUrl, long createdAt) {
        this.id = id;
        this.longUrl = lonUrl;
        this.createdAt = createdAt;
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
        Redirection redirection = (Redirection) o;
        return Objects.equals(id, redirection.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
