package org.npathai.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.npathai.api.DirectExecutorJobService;
import org.npathai.zookeeper.TestingZkManager;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

class IdGenerationServiceTest {
    private static final String NEXT_ID_ZNODE = "/next-id";

    private IdGenerationService idGenerationService;
    private TestingZkManager testingZkManager;
    @Spy
    private DirectExecutorJobService directExecutorJobService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testingZkManager = new TestingZkManager();
        testingZkManager.setData(NEXT_ID_ZNODE, Id.first().encode().getBytes());
        this.idGenerationService = new IdGenerationService(testingZkManager, directExecutorJobService);
    }

    @Test
    /**
     * Cannot prove uniqueness just like we cannot prove Randomness
     */
    public void returnsUniqueIds() {
        Assertions.assertThat(IntStream.range(1, 101)
                .mapToObj(i -> idGenerationService.nextId())
                .collect(Collectors.toSet())
                .size()).isEqualTo(100);
    }

    @Nested
    public class HydrationTests {

        public static final int BATCH_SIZE = 10;

        @Test
        public void triggersHydrationEagerlyOnConstruction() {
            verify(directExecutorJobService).submit(any());
        }

        @Test
        public void rehydratesTheCacheWhenCachedIdCountDropsBelowThresholdOfFiveIds() {
            reset(directExecutorJobService);
            useFiveIds();
            verifyZeroInteractions(directExecutorJobService);

            idGenerationService.nextId();
            verify(directExecutorJobService).submit(any());
        }

        private void useFiveIds() {
            IntStream.range(1, 6)
                    .forEach(unused -> idGenerationService.nextId());
        }

        @Test
        public void updatesNextAvailableIdInZookeeperAccordingToBatchSize() throws Exception {
            Assertions.assertThat(new String(testingZkManager.getData(NEXT_ID_ZNODE)))
                    .isEqualTo(Id.first().incrementAndGet(BATCH_SIZE).encode());
        }
    }
}