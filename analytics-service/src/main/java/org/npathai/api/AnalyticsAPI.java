package org.npathai.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import org.npathai.dao.DataAccessException;
import org.npathai.domain.AnalyticsService;
import org.npathai.model.AnalyticsInfo;
import org.npathai.model.UserInfo;

import java.util.Optional;

@Controller
public class AnalyticsAPI {

    private final AnalyticsService analyticsService;

    public AnalyticsAPI(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Secured("isAuthenticated()")
    @Post("/analytics/{id}")
    public void post(String id) {
        analyticsService.onRedirectionCreated(id);
    }

    @Get("/analytics/{id}")
    @Secured("isAuthenticated()")
    public HttpResponse<AnalyticsInfo> get(Authentication authentication, String id) throws DataAccessException {
        UserInfo userInfo = UserInfo.fromAuthentication(authentication);
        Optional<AnalyticsInfo> info;
        info = analyticsService.getById(userInfo, id);

        if (info.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(info.get());
    }
}
