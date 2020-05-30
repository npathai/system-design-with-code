package org.npathai.domain;

import org.npathai.dao.UrlDao;
import org.npathai.model.ShortUrl;
import org.npathai.service.IdGenerationService;

public class UrlShortener {

    private final IdGenerationService idGenerationService;
    private UrlDao dao;

    public UrlShortener(IdGenerationService idGenerationService, UrlDao dao) {
        this.idGenerationService = idGenerationService;
        this.dao = dao;
    }

    public ShortUrl shorten(String longUrl) {
        // Remote call
        String id = idGenerationService.getId();
        ShortUrl shortUrl = new ShortUrl(id, longUrl);
        dao.save(shortUrl);
        return shortUrl;
    }

    public String expand(String id) {
        return dao.getById(id);
    }

}
