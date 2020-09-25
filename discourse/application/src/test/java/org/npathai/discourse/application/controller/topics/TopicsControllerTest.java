package org.npathai.discourse.application.controller.topics;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.npathai.discourse.application.domain.topics.Topic;
import org.npathai.discourse.application.domain.topics.TopicService;

import javax.inject.Inject;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@MicronautTest
public class TopicsControllerTest {

    public static final int USER_ID = 1;

    @Inject
    TopicService topicService;

    @Inject
    TopicControllerClient topicControllerClient;
    Topic topic;

    @BeforeEach
    public void setUp() {
        topic = new Topic();
        topic.setCategoryId(1);
        topic.setId(2);
        topic.setName("Test topic");
        topic.setUserId(USER_ID);
        when(topicService.getTopics(USER_ID)).thenReturn(List.of(topic));
    }

    @Test
    public void returnsTopicsForUser() {
        List<Topic> topics = topicControllerClient.get();

        assertReflectionEquals(List.of(topic), topics);
    }

    @MockBean(TopicService.class)
    public TopicService createMockTopicService() {
        return Mockito.mock(TopicService.class);
    }
}
