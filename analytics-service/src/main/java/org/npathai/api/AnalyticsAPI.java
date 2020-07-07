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

// FIXME converting APIs to anonymous, as we need service-service token between short-url-generator and this service.
// Need to find out how to achieve it using micronaut.
@Controller
public class AnalyticsAPI {

    private final AnalyticsService analyticsService;

    public AnalyticsAPI(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Secured("isAnonymous()")
    @Post("/analytics/{id}")
    public void post(String id) {
        analyticsService.onRedirectionCreated(id);
    }

    @Secured("isAnonymous()")
    @Post("/analytics/{id}/click")
    public void redirectionClicked(String id) {
        analyticsService.onRedirectionClicked(id);
    }

    @Get("/analytics/{id}")
    @Secured("isAnonymous()")
    public HttpResponse<AnalyticsInfo> get(String id) throws DataAccessException {
        Optional<AnalyticsInfo> info;
        info = analyticsService.getById(null, id);

        if (info.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(info.get());
    }
}
