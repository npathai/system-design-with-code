package org.npathai.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.annotation.Nullable;
import java.time.Clock;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Redirection {
    private String id;
    private String longUrl;
    private long createdAtMillis;
    private long expiryAtMillis;
    private String uid;

    public Redirection() {

    }

    public Redirection(String id, String longUrl, long createdAtMillis, long expiryAtMillis) {
        this(id, longUrl, createdAtMillis, expiryAtMillis, null);
    }

    public Redirection(String id, String longUrl, long createdAtMillis, long expiryAtMillis, String uid) {
        this.id = id;
        this.longUrl = longUrl;
        this.createdAtMillis = createdAtMillis;
        this.expiryAtMillis = expiryAtMillis;
        this.uid = uid;
    }

    public String id() {
        return id;
    }

    public String longUrl() {
        return longUrl;
    }

    public long createdAtMillis() {
        return createdAtMillis;
    }

    @Nullable
    public String uid() {
        return uid;
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

    public long expiryAtMillis() {
        return expiryAtMillis;
    }

    public boolean isExpired(Clock clock) {
        return expiryAtMillis <= clock.millis();
    }

    @Override
    public String toString() {
        return "Redirection{" +
                "id='" + id + '\'' +
                ", longUrl='" + longUrl + '\'' +
                ", createdAtMillis=" + createdAtMillis +
                ", expiryAtMillis=" + expiryAtMillis +
                ", uid='" + uid + '\'' +
                '}';
    }

    public boolean isAnonymous() {
        return uid == null;
    }
}
