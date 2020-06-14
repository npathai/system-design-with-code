package org.npathai.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.api.ShortenRequest;
import org.npathai.cache.RedirectionCache;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

public class UrlShortener {
    private static final Logger LOG = LogManager.getLogger(UrlShortener.class);

    private final Properties applicationProperties;
    private final IdGenerationServiceClient idGenerationServiceClient;
    private final Clock clock;
    private RedirectionDao dao;
    private final RedirectionCache redirectionCache;

    public UrlShortener(
            Properties applicationProperties,
            IdGenerationServiceClient idGenerationServiceClient,
            RedirectionDao dao,
            RedirectionCache redirectionCache,
            Clock clock) {
        this.applicationProperties = applicationProperties;
        this.idGenerationServiceClient = idGenerationServiceClient;
        this.dao = dao;
        this.redirectionCache = redirectionCache;
        this.clock = clock;
    }

    public Redirection shorten(ShortenRequest shortenRequest) throws Exception {
        // Remote call
        String id = idGenerationServiceClient.generateId();
        long creationTime = clock.millis();
        long expiryTime = creationTime + lifetimeInMillis();
        Redirection redirection = new Redirection(id, shortenRequest.getLongUrl(), creationTime, expiryTime);
        dao.save(redirection);
        LOG.info("Created new redirection. " + redirection);
        return redirection;
    }

    private long lifetimeInMillis() {
        return Duration.ofSeconds(Integer.parseInt(applicationProperties.getProperty(
                "anonymousUrlLifetimeInSeconds"))).toMillis();
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
                LOG.info("Redirection with id: " + id + " found in cache and is not expired.");
                return cachedRedirection.map(Redirection::longUrl);
            }

            LOG.info(String.format("Expiry Time: %s <= %s",
                    new Timestamp(cachedRedirection.get().expiryTimeInMillis()),
                    new Timestamp(clock.millis())));
            LOG.info("Redirection with id: " + id + " found in cache & is expired.");

            redirectionCache.deleteById(id);
            dao.deleteById(id);
            return Optional.empty();
        }

        // This is first time url is being accessed
        LOG.info("Redirection with id: " + id + " accessed for first time");
        Optional<Redirection> optionalRedirection = dao.getById(id);
        if (!optionalRedirection.isPresent()) {
            LOG.info("Redirection with id: " + id + " is unknown");
            return Optional.empty();
        }

        Redirection redirection = optionalRedirection.get();
        if (redirection.isExpired(clock)) {
            LOG.info(String.format("Expiry Time: %s <= %s",
                    new Timestamp(redirection.expiryTimeInMillis()),
                    new Timestamp(clock.millis())));
            LOG.info("Redirection with id: " + id + " is expired.");
            dao.deleteById(id);
            return Optional.empty();
        }

        LOG.info("Saving redirection with id: " + id + " in cache");
        redirectionCache.put(redirection);
        return optionalRedirection.map(Redirection::longUrl);
    }
}
