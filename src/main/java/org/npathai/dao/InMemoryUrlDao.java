package org.npathai.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlDao implements UrlDao {
    private Map<String, String> shortToLong = new ConcurrentHashMap<>();

    @Override
    public void save(String id, String longUrl) {
        shortToLong.put(id, longUrl);
    }

    @Override
    public String get(String id) {
        return shortToLong.get(id);
    }
}
