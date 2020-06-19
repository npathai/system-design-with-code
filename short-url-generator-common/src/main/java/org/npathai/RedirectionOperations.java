package org.npathai;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;

public interface RedirectionOperations {

    @Secured("isAnonymous()")
    @Get("/{id}")
    HttpResponse<String> handle(String id) throws Exception;
}
