package org.npathai.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.dao.AnalyticsDao;
import org.npathai.dao.DataAccessException;
import org.npathai.model.AnalyticsInfo;
import org.npathai.model.UserInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnalyticsService {
    private static final Logger LOG = LogManager.getLogger(AnalyticsService.class);
    private final AnalyticsDao dao;

    public AnalyticsService(AnalyticsDao dao) {
        this.dao = dao;
    }

    public void onRedirectionClicked(String id) {
        dao.incrementClick(id);
    }

    public Optional<AnalyticsInfo> getById(UserInfo userInfo, String id) throws DataAccessException {
        // FIXME handle this scenario
//        if (!redirection.get().uid().equals(userInfo.uid())) {
//            throw new UnauthorizedAccessException();
//        }

        return dao.getById(id).or(() -> Optional.of(new AnalyticsInfo()));
    }

    public void onRedirectionCreated(String id) {
        dao.save(new AnalyticsInfo(id));
    }
}
