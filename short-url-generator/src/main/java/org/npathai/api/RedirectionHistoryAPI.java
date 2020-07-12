package org.npathai.api;

import com.google.common.collect.Lists;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import org.npathai.dao.DataAccessException;
import org.npathai.domain.RedirectionHistoryService;
import org.npathai.metrics.ServiceTags;
import org.npathai.model.Redirection;
import org.npathai.model.UserInfo;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RedirectionHistoryAPI {

    public static final String USER_REDIRECTION_HISTORY_API_ENDPOINT = "/user/redirection_history";
    private final RedirectionHistoryService redirectionHistoryService;
    private final MeterRegistry meterRegistry;

    public RedirectionHistoryAPI(RedirectionHistoryService redirectionHistoryService, MeterRegistry meterRegistry) {
        this.redirectionHistoryService = redirectionHistoryService;
        this.meterRegistry = meterRegistry;
    }

    @Secured("isAuthenticated()")
    @Get(USER_REDIRECTION_HISTORY_API_ENDPOINT)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Redirection> getRedirectionHistory(@Nonnull Authentication authentication) throws DataAccessException {
        Tags commonTags = ServiceTags.httpApiTags("short.url.generator",  "redirection.history",
                USER_REDIRECTION_HISTORY_API_ENDPOINT, HttpMethod.GET);

        meterRegistry.counter("http.requests.total", commonTags).increment();

        try {
            UserInfo userInfo = UserInfo.fromAuthentication(authentication);
            List<Redirection> redirectionHistory = redirectionHistoryService.getRedirectionHistory(userInfo);

            meterRegistry.counter("http.responses.total", ServiceTags.httpOkStatusTags(commonTags)).increment();
            return redirectionHistory;
        } catch (DataAccessException ex) {
            meterRegistry.counter("http.responses.total",
                    ServiceTags.httpInternalErrorStatusTags(commonTags)).increment();
            throw ex;
        }
    }
}
