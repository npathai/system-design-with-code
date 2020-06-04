package org.npathai.domain;

import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.client.IdGenerationServiceClient;

import java.util.Optional;

public class UrlShortener {

    private final IdGenerationServiceClient idGenerationServiceClient;
    private RedirectionDao dao;

    public UrlShortener(IdGenerationServiceClient idGenerationServiceClient, RedirectionDao dao) {
        this.idGenerationServiceClient = idGenerationServiceClient;
        this.dao = dao;
    }

    public Redirection shorten(String longUrl) throws Exception {
        // Remote call
        String id = idGenerationServiceClient.getId();
        Redirection redirection = new Redirection(id, longUrl);
        dao.save(redirection);
        return redirection;
    }

    public Optional<Redirection> expand(String id) throws Exception {
        return dao.getById(id);
    }
}
