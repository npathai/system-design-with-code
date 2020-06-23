package org.npathai.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import org.npathai.dao.DataAccessException;
import org.npathai.domain.RedirectionHistoryService;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import javax.annotation.Nonnull;
import java.util.List;

@Controller
public class RedirectionHistoryAPI {

    private final RedirectionHistoryService redirectionHistoryService;

    public RedirectionHistoryAPI(RedirectionHistoryService redirectionHistoryService) {
        this.redirectionHistoryService = redirectionHistoryService;
    }

    @Secured("isAuthenticated()")
    @Get("/user/redirection_history")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Redirection> getRedirectionHistory(@Nonnull Authentication authentication) throws DataAccessException {
        UserInfo userInfo = UserInfo.fromAuthentication(authentication);
        return redirectionHistoryService.getRedirectionHistory(userInfo);
    }
}
