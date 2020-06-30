package org.npathai.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.dao.AnalyticsDao;
import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.AnalyticsInfo;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnalyticsService {
    private static final Logger LOG = LogManager.getLogger(AnalyticsService.class);
    private final AnalyticsDao dao;
    private final UrlShortener urlShortener;

    public AnalyticsService(UrlShortener urlShortener, AnalyticsDao dao) {
        this.urlShortener = urlShortener;
        this.dao = dao;
    }

    public void onSuccessfulRedirection(String id, String longUrl, Map<String, List<String>> headers) {
        LOG.info("Redirection [{}] -> Request: [{}]", id, headers);
        dao.incrementClick(id);
    }

    public Optional<AnalyticsInfo> getById(UserInfo userInfo, String id) throws DataAccessException, UnauthorizedAccessException {
        Optional<Redirection> redirection = urlShortener.getById(id);
        if (redirection.isEmpty()) {
            return Optional.empty();
        }

        if (!redirection.get().uid().equals(userInfo.uid())) {
            throw new UnauthorizedAccessException();
        }

        return dao.getById(id).or(() -> Optional.of(new AnalyticsInfo()));
    }
}
