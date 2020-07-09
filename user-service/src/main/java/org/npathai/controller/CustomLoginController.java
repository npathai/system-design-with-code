package org.npathai.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.authentication.Authenticator;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.endpoints.LoginController;
import io.micronaut.security.handlers.LoginHandler;
import io.reactivex.Single;

import javax.validation.Valid;

@Controller
@Replaces(LoginController.class)
public class CustomLoginController extends LoginController {

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
        meterRegistry.counter("web.access.controller.user.service.login.request").increment();
        return super.login(usernamePasswordCredentials, request)
                .map(loginResponse -> {
                    if (loginResponse.status() != HttpStatus.UNAUTHORIZED) {
                        meterRegistry.counter("web.access.controller.user.service.login.successful").increment();
                    } else {
                        meterRegistry.counter("web.access.controller.user.service.login.failed").increment();
                    }
                    return loginResponse;
                });
    }
}
