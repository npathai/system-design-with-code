package org.npathai.domain;

import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.util.List;
import java.util.stream.Collectors;

public class RedirectionHistoryService {

    private final RedirectionDao redirectionDao;

    public RedirectionHistoryService(RedirectionDao redirectionDao) {
        this.redirectionDao = redirectionDao;
    }

    public List<Redirection> getRedirectionHistory(@Nonnull UserInfo userInfo) throws DataAccessException {
        return redirectionDao.getAllByUser(userInfo.uid())
                .stream()
                .filter(redirection -> !redirection.isExpired(Clock.systemDefaultZone()))
                .collect(Collectors.toList());
    }
}
