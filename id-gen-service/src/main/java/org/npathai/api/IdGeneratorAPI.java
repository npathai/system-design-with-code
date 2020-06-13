package org.npathai.api;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.npathai.domain.IdGenerationService;

import javax.inject.Inject;

@Controller("/generate")
public class IdGeneratorAPI {

    @Inject
    private IdGenerationService idGenerationService;

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String generateId() {
        return idGenerationService.nextId();
    }
}
