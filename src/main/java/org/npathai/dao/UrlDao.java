package org.npathai.dao;

public interface UrlDao {
    void save(String shortUrl, String longUrl);
    String get(String shortUrl);
}
