package org.npathai.domain;

import org.npathai.cache.RedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;

public class UrlShortener {

    private final IdGenerationServiceClient idGenerationServiceClient;
    private final Clock clock;
    private RedirectionDao dao;
    private final RedirectionCache redirectionCache;

    public UrlShortener(IdGenerationServiceClient idGenerationServiceClient,
                        RedirectionDao dao,
                        RedirectionCache redirectionCache,
                        Clock clock) {
        this.idGenerationServiceClient = idGenerationServiceClient;
        this.dao = dao;
        this.redirectionCache = redirectionCache;
        this.clock = clock;
    }

    public Redirection shorten(String longUrl) throws Exception {
        // Remote call
        String id = idGenerationServiceClient.getId();
        long creationTime = clock.millis();
        long expiryTime = creationTime + Duration.ofMinutes(1).toMillis();
        Redirection redirection = new Redirection(id, longUrl, creationTime, expiryTime);
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
        Optional<Redirection> cachedRedirection = redirectionCache.getById(id);
        if (cachedRedirection.isPresent()) {
            if (!cachedRedirection.get().isExpired(clock)) {
                return cachedRedirection.map(Redirection::longUrl);
            }

            // Is expired so remove from db and cache
            redirectionCache.deleteById(id);
            dao.deleteById(id);
            return Optional.empty();
        }

        // This is first time url is being accessed
        Optional<Redirection> optionalRedirection = dao.getById(id);

        if (!optionalRedirection.isPresent()) {
            return Optional.empty();
        }

        Redirection redirection = optionalRedirection.get();
        if (redirection.isExpired(clock)) {
            dao.deleteById(id);
            return Optional.empty();
        }

        redirectionCache.put(redirection);
        return optionalRedirection.map(Redirection::longUrl);
    }

    private boolean isExpired(Long expiryTimeInMillis) {
        return expiryTimeInMillis <= clock.millis();
    }
}
