package org.npathai.dao;

import org.npathai.model.ShortUrl;

public interface UrlDao {
    void save(ShortUrl shortUrl);
    String getById(String id);
}
