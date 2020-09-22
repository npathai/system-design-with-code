package org.npathai.discourse.application.controller.topics;

import io.micronaut.http.client.annotation.Client;
import org.glassfish.hk2.api.messaging.Topic;

import java.util.List;

@Client("/topics")
public interface TopicControllerClient {
    List<Topic> get();
}