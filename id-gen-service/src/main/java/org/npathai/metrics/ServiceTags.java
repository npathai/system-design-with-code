package org.npathai.metrics;

import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpStatus;

public class ServiceTags {

    public static Tags httpApiTags(String serviceName, String serviceModule, String api, HttpMethod httpMethod) {
        return Tags.of(Tag.of("service", serviceName), Tag.of("service.module", serviceModule),
                Tag.of("api", api),
                Tag.of("http.method", httpMethod.name()));
    }

    public static Tags httpOkStatusTags(Tags commonTags) {
        return commonTags.and(ServiceTags.httpStatusTags(commonTags, HttpStatus.OK));
    }

    public static Tags httpInternalErrorStatusTags(Tags commonTags) {
        return commonTags.and(ServiceTags.httpStatusTags(commonTags, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public static Tags httpNotFoundStatusTags(Tags commonTags) {
        return commonTags.and(ServiceTags.httpStatusTags(commonTags, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public static Tags httpStatusTags(Tags commonTags, HttpStatus httpStatus) {
        return commonTags.and(Tag.of("http.status", String.valueOf(httpStatus.getCode())));
    }
}
