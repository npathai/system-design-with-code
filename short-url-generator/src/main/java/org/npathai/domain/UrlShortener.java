package org.npathai.domain;

import org.npathai.dao.UrlDao;
import org.npathai.model.ShortUrl;
import org.npathai.client.IdGenerationServiceClient;

public class UrlShortener {

    private final IdGenerationServiceClient idGenerationServiceClient;
    private UrlDao dao;

    public UrlShortener(IdGenerationServiceClient idGenerationServiceClient, UrlDao dao) {
        this.idGenerationServiceClient = idGenerationServiceClient;
        this.dao = dao;
    }

    public ShortUrl shorten(String longUrl) throws Exception {
        // Remote call
        String id = idGenerationServiceClient.getId();
        ShortUrl shortUrl = new ShortUrl(id, longUrl);
        dao.save(shortUrl);
        return shortUrl;
    }

    public String expand(String id) {
        return dao.getById(id);
    }

}
