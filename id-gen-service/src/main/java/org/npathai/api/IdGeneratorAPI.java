package org.npathai.api;

import io.micrometer.core.instrument.MeterRegistry;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.npathai.domain.IdGenerationService;

import javax.inject.Inject;

@Controller("/generate")
public class IdGeneratorAPI {

    private final IdGenerationService idGenerationService;
    private final MeterRegistry meterRegistry;

    public IdGeneratorAPI(IdGenerationService idGenerationService, MeterRegistry meterRegistry) {
        this.idGenerationService = idGenerationService;
        this.meterRegistry = meterRegistry;
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String generateId() {
        meterRegistry.counter("web.access.controller.id.service.generation.request").increment();
        String id = idGenerationService.nextId();
        meterRegistry.counter("web.access.controller.id.service.generation.successful").increment();
        return id;
    }
}
