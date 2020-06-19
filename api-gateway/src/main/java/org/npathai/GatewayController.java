package org.npathai;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.reactivex.Single;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@Controller
public class GatewayController implements UrlShortenerOperations, RedirectionOperations, AuthenticationOperations {
    private static final Logger LOG = LogManager.getLogger(GatewayController.class);

    private LoginClient loginClient;
    private ShortUrlClient shortUrlClient;

    public GatewayController(LoginClient loginClient, ShortUrlClient shortUrlClient) {
        this.loginClient = loginClient;
        this.shortUrlClient = shortUrlClient;
    }

    @Override
    public Single<HttpResponse<BearerAccessRefreshToken>> login(@Valid UsernamePasswordCredentials usernamePasswordCredentials, HttpRequest<?> request) {
        return loginClient.login(usernamePasswordCredentials, request);
    }

    @Override
    public String shorten(@Nullable Principal principal, ShortenRequest shortenRequest) throws Exception {
        return shortUrlClient.shorten(principal, shortenRequest);
    }

    @Override
    public HttpResponse<String> handle(String id) throws Exception {
        HttpResponse<String> remoteResponse = shortUrlClient.handle(id);
        if (remoteResponse.status() == HttpStatus.OK) {
            return HttpResponse.redirect(URI.create(remoteResponse.body()));
        }

        return remoteResponse;
    }
}
