package org.npathai.discourse.application.controller.topics;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.npathai.discourse.application.domain.topics.Topic;

import java.util.List;

@Client("/topics")
public interface TopicControllerClient {
    @Get
    List<Topic> get();
}