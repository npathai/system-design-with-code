package org.npathai;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Single;

import javax.validation.Valid;

public interface AuthenticationOperations {

    @Secured("isAnonymous()")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    @Post("/login")
    Single<HttpResponse<BearerAccessRefreshToken>> login(@Valid @Body UsernamePasswordCredentials usernamePasswordCredentials,
                                                         HttpRequest<?> request);
}
