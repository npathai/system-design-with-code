package org.npathai.domain;

import org.npathai.dao.DataAccessException;
import org.npathai.dao.RedirectionDao;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import javax.annotation.Nonnull;
import java.util.List;

public class RedirectionHistoryService {

    private final RedirectionDao redirectionDao;

    public RedirectionHistoryService(RedirectionDao redirectionDao) {
        this.redirectionDao = redirectionDao;
    }

    public List<Redirection> getRedirectionHistory(@Nonnull UserInfo userInfo) throws DataAccessException {
        return redirectionDao.getAllByUser(userInfo.uid());
    }
}
