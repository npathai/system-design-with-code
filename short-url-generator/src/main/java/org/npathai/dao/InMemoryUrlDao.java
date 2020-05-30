package org.npathai.dao;

import org.npathai.model.ShortUrl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUrlDao implements UrlDao {
    private Map<String, ShortUrl> shortToLong = new ConcurrentHashMap<>();

    @Override
    public void save(ShortUrl shortUrl) {
        shortToLong.put(shortUrl.id(), shortUrl);
    }

    @Override
    public String getById(String id) {
        return shortToLong.get(id).longUrl();
    }
}
