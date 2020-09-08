package org.npathai.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.authentication.Authenticator;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.endpoints.LoginController;
import io.micronaut.security.handlers.LoginHandler;
import io.reactivex.Single;
import org.npathai.metrics.ServiceTags;

import javax.validation.Valid;

@Controller
@Replaces(LoginController.class)
public class CustomLoginController extends LoginController {

    private static final String LOGIN_ENDPOINT = "/login";
    private final MeterRegistry meterRegistry;

    /**
     * @param authenticator  {@link Authenticator} collaborator
     * @param loginHandler   A collaborator which helps to build HTTP response depending on success or failure.
     * @param eventPublisher The application event publisher
     */
    public CustomLoginController(Authenticator authenticator, LoginHandler loginHandler,
                                 ApplicationEventPublisher eventPublisher, MeterRegistry meterRegistry) {
        super(authenticator, loginHandler, eventPublisher);
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Single<HttpResponse> login(@Valid UsernamePasswordCredentials usernamePasswordCredentials, HttpRequest<?> request) {
        Tags commonTags = ServiceTags.httpApiTags("user.service",  "authentication",
                LOGIN_ENDPOINT, HttpMethod.POST);

        meterRegistry.counter("http.requests.total", commonTags).increment();
        return super.login(usernamePasswordCredentials, request)
                .map(loginResponse -> {
                    meterRegistry.counter("http.responses.total",
                            ServiceTags.httpStatusTags(commonTags, loginResponse.getStatus())).increment();
                    return loginResponse;
                });
    }
}
