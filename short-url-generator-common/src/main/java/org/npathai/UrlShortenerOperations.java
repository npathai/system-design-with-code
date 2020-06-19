package org.npathai;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;

import javax.annotation.Nullable;
import java.security.Principal;

public interface UrlShortenerOperations {

    @Secured("isAnonymous()")
    @Post("/shorten")
    @Produces(MediaType.APPLICATION_JSON)
    String shorten(@Nullable Principal principal, @Body ShortenRequest shortenRequest) throws Exception;
}
