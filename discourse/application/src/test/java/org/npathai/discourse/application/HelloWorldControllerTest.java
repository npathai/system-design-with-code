package org.npathai.discourse.application;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class HelloWorldControllerTest {

    @Inject
    HelloWorldControllerClient helloWorldControllerClient;

    @Test
    public void returnsWelcomeMessage() {
        String message = helloWorldControllerClient.helloWorld();

        assertThat(message).isEqualTo("Welcome to Discourse!");
    }
}
