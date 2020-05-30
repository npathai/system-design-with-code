package org.npathai.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlDao implements UrlDao {
    private Map<String, String> shortToLong = new ConcurrentHashMap<>();

    @Override
    public void save(String shortUrl, String longUrl) {
        shortToLong.put(shortUrl, longUrl);
    }

    @Override
    public String get(String shortUrl) {
        return shortToLong.get(shortUrl);
    }
}
