package org.npathai.api;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.npathai.client.IdGenerationServiceClient;

import javax.inject.Singleton;

@Singleton
@Requires(env = Environment.TEST)
@Replaces(IdGenerationServiceClient.class)
public class IdGenerationServiceStub implements IdGenerationServiceClient {

    private String id;

    @Override
    public String generateId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
