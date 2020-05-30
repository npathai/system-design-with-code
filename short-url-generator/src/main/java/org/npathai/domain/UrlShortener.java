package org.npathai.domain;

import org.npathai.dao.UrlDao;
import org.npathai.model.ShortUrl;

public class UrlShortener {

    private final ShortUrlGenerator shortUrlGenerator = new ShortUrlGenerator();

    private UrlDao dao;

    public UrlShortener(UrlDao dao) {
        this.dao = dao;
    }

    public ShortUrl shorten(String longUrl) {
        String id = shortUrlGenerator.generate();
        ShortUrl shortUrl = new ShortUrl(id, longUrl);
        dao.save(shortUrl);
        return shortUrl;
    }

    public String expand(String id) {
        return dao.getById(id);
    }

}
