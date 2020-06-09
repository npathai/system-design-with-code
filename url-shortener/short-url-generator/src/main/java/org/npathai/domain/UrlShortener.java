package org.npathai.domain;

import org.npathai.cache.RedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;

import java.util.Optional;

public class UrlShortener {

    private final IdGenerationServiceClient idGenerationServiceClient;
    private RedirectionDao dao;
    private final RedirectionCache redirectionCache;

    public UrlShortener(IdGenerationServiceClient idGenerationServiceClient,
                        RedirectionDao dao,
                        RedirectionCache redirectionCache) {
        this.idGenerationServiceClient = idGenerationServiceClient;
        this.dao = dao;
        this.redirectionCache = redirectionCache;
    }

    public Redirection shorten(String longUrl) throws Exception {
        // Remote call
        String id = idGenerationServiceClient.getId();
        Redirection redirection = new Redirection(id, longUrl);
        dao.save(redirection);
        return redirection;
    }

    public Optional<Redirection> getById(String id) throws DataAccessException {
        return dao.getById(id);
    }

    /**
     * Should be used to get redirection url
     */
    public Optional<String> expand(String id) throws Exception {
        Optional<String> cachedRedirection = redirectionCache.get(id);
        if (cachedRedirection.isPresent()) {
            return cachedRedirection;
        }

        Optional<Redirection> redirection = dao.getById(id);

        redirection.ifPresent(it -> redirectionCache.put(it.id(), it.longUrl()));
        return redirection.map(Redirection::longUrl);
    }
}
