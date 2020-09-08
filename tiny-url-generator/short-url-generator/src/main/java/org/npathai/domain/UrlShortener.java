package org.npathai.domain;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.api.ShortenRequest;
import org.npathai.cache.RedirectionCache;
import org.npathai.client.AnalyticsServiceClient;
import org.npathai.client.IdGenerationServiceClient;
import org.npathai.config.UrlLifetimeConfiguration;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.metrics.ServiceTags;
import org.npathai.model.Redirection;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.util.Optional;

public class UrlShortener {
    private static final Logger LOG = LogManager.getLogger(UrlShortener.class);

    private final IdGenerationServiceClient idGenerationServiceClient;
    private final AnalyticsServiceClient analyticsServiceClient;
    private final Clock clock;
    private final MeterRegistry meterRegistry;
    private final UrlLifetimeConfiguration urlLifetimeConfiguration;
    private RedirectionDao dao;
    private final RedirectionCache redirectionCache;

    public UrlShortener(
            UrlLifetimeConfiguration urlLifetimeConfiguration,
            IdGenerationServiceClient idGenerationServiceClient,
            AnalyticsServiceClient analyticsServiceClient,
            RedirectionDao dao,
            RedirectionCache redirectionCache,
            Clock clock,
            MeterRegistry meterRegistry) {
        this.urlLifetimeConfiguration = urlLifetimeConfiguration;
        this.idGenerationServiceClient = idGenerationServiceClient;
        this.analyticsServiceClient = analyticsServiceClient;
        this.dao = dao;
        this.redirectionCache = redirectionCache;
        this.clock = clock;
        this.meterRegistry = meterRegistry;
    }

    public Redirection shorten(ShortenRequest shortenRequest) throws Exception {
        Redirection redirection = createRedirection(shortenRequest);
        if (!shortenRequest.isAnonymous()) {
            analyticsServiceClient.redirectionCreated(redirection.id());
        }
        LOG.info("Created new redirection. " + redirection);
        return redirection;
    }

    private Redirection createRedirection(ShortenRequest shortenRequest) throws DataAccessException {
        // Remote call
        String id = idGenerationServiceClient.generateId();
        long creationTime = clock.millis();
        Redirection redirection;
        if (shortenRequest.isAnonymous()) {
            redirection = createAnonymousRedirection(shortenRequest, id, creationTime);
        } else {
            redirection = createAuthenticatedRedirection(shortenRequest, id, creationTime);
        }
        dao.save(redirection);
        return redirection;
    }

    private Redirection createAuthenticatedRedirection(ShortenRequest shortenRequest, String id, long creationTime) {
        long expiryTime = creationTime + lifetimeInMillis(false);
        return new Redirection(id, shortenRequest.getLongUrl(), creationTime, expiryTime,
                shortenRequest.getUserInfo().uid());
    }

    private Redirection createAnonymousRedirection(ShortenRequest shortenRequest, String id, long creationTime) {
        long expiryTime = creationTime + lifetimeInMillis(true);
        return new Redirection(id, shortenRequest.getLongUrl(), creationTime, expiryTime);
    }

    private long lifetimeInMillis(boolean isAnonymous) {
        int lifetimeInSeconds = isAnonymous? Integer.parseInt(urlLifetimeConfiguration.getAnonymous())
                : Integer.parseInt(urlLifetimeConfiguration.getAuthenticated());

        return Duration.ofSeconds(lifetimeInSeconds).toMillis();
    }

    public Optional<Redirection> expand(String id) throws Exception {
        Tags tags = ServiceTags.serviceTags("short.url.generator", "url.shortener",
                "expand");

        meterRegistry.counter("url.redirection.requests.total", tags).increment();

        Optional<Redirection> cachedRedirection = redirectionCache.getById(id);
        if (cachedRedirection.isPresent()) {
            tags = tags.and("cache.hit", "true");

            if (!cachedRedirection.get().isExpired(clock)) {
                LOG.info("Redirection with id: " + id + " found in cache and is not expired.");
                tags = tags.and("redirection.status", "active");
                meterRegistry.counter("url.redirection.responses.total", tags).increment();
                return cachedRedirection;
            }


            LOG.info(String.format("Expiry Time: %s <= %s",
                    new Timestamp(cachedRedirection.get().expiryAtMillis()),
                    new Timestamp(clock.millis())));
            LOG.info("Redirection with id: " + id + " found in cache & is expired.");

            redirectionCache.deleteById(id);
            dao.deleteById(id);
            tags = tags.and("redirection.status", "active");
            meterRegistry.counter("url.redirection.responses.total", tags).increment();
            return Optional.empty();
        }

        tags = tags.and("cache.hit", "false");

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
                    new Timestamp(redirection.expiryAtMillis()),
                    new Timestamp(clock.millis())));
            dao.deleteById(id);
            LOG.info("Redirection with id: " + id + " is expired.");
            tags = tags.and("redirection.status", "expired");
            meterRegistry.counter("url.redirection.responses.total", tags).increment();
            return Optional.empty();
        }

        redirectionCache.put(redirection);
        LOG.info("Saved redirection with id: " + id + " in cache");
        tags = tags.and("redirection.status", "active");
        meterRegistry.counter("url.redirection.responses.total", tags).increment();
        return optionalRedirection;
    }
}
