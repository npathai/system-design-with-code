package org.npathai.dao;

public interface UrlDao {
    void save(String id, String longUrl);
    String get(String id);
}
