package org.npathai.api;

import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.npathai.annotations.ApiTest;
import org.npathai.domain.Id;
import org.npathai.zookeeper.TestingZkManager;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Inject;

@ApiTest
@MicronautTest
class IdGeneratorAPITest {

    private static final String NEXT_ID_ZNODE = "/next-id";

    @Inject
    ZkManager testingZkManager;

    @Inject
    IdGeneratorClient client;

    @Test
    public void returnsIdOfLengthFiveInTextPlainContentType() {
        String generatedId = client.generate();
        Assertions.assertThat(generatedId)
                .isNotNull()
                .hasSize(5);
    }

    @MockBean(ZkManager.class)
    public ZkManager createTestingZkManager() throws Exception {
        TestingZkManager testingZkManager = new TestingZkManager();
        testingZkManager.setData(NEXT_ID_ZNODE, Id.first().encode().getBytes());
        return testingZkManager;
    }
}