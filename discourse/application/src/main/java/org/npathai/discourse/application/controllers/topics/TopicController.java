package org.npathai.discourse.application.controllers.topics;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.npathai.discourse.application.domain.topics.Topic;
import org.npathai.discourse.application.domain.topics.TopicService;

import java.util.List;

@Controller("/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @Get
    public List<Topic> getAll() {
        // FIXME for now assuming there is single user. It should be id of user authenticated using current
        // auth token
        return topicService.getTopics(1);
    }
}
