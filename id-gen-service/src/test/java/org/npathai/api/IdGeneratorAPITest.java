package org.npathai.api;

import io.micronaut.test.annotation.MicronautTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.npathai.annotations.ApiTest;
import org.npathai.domain.Id;

import javax.inject.Inject;

@ApiTest
@MicronautTest
class IdGeneratorAPITest {

    private static final String NEXT_ID_ZNODE = "/next-id";

    @Inject
    TestingZkManager testingZkManager;

    @Inject
    IdGeneratorClient client;

    @BeforeEach
    public void setup() throws Exception {
        testingZkManager.setData(NEXT_ID_ZNODE, Id.first().encode().getBytes());
    }

    @Test
    public void returnsIdOfLengthFiveInTextPlainContentType() {
        String generatedId = client.generate();
        Assertions.assertThat(generatedId)
                .isNotNull()
                .hasSize(5);
    }
}