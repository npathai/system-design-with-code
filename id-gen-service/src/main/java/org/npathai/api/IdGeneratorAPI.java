package org.npathai.api;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.npathai.domain.IdGenerationService;
import org.npathai.metrics.ServiceTags;

@Controller(IdGeneratorAPI.GENERATION_API_ENDPOINT)
public class IdGeneratorAPI {

    public static final String GENERATION_API_ENDPOINT = "/generate";
    private final IdGenerationService idGenerationService;
    private final MeterRegistry meterRegistry;

    public IdGeneratorAPI(IdGenerationService idGenerationService, MeterRegistry meterRegistry) {
        this.idGenerationService = idGenerationService;
        this.meterRegistry = meterRegistry;
    }

    @Get
    @Produces(MediaType.TEXT_PLAIN)
    public String generateId() {
        Tags commonTags = ServiceTags.httpApiTags("id.generation",  "generation",
                GENERATION_API_ENDPOINT, HttpMethod.GET);

        meterRegistry.counter("http.requests.total", commonTags).increment();

        String id = idGenerationService.nextId();

        meterRegistry.counter("http.responses.total", ServiceTags.httpOkStatusTags(commonTags)).increment();
        return id;
    }
}
