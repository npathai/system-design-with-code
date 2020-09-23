package org.npathai.discourse.application.controller.topics;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.npathai.discourse.application.domain.topics.TopicService;

import javax.inject.Inject;

@MicronautTest
public class TopicsControllerTest {

    @Inject
    TopicService topicService;

    @Inject
    TopicControllerClient topicControllerClient;

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void returnsTopicsForUser() {

    }

    @MockBean(TopicService.class)
    public TopicService createMockTopicService() {
        return Mockito.mock(TopicService.class);
    }
}
