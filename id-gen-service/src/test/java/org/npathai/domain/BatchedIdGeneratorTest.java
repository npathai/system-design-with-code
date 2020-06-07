package org.npathai.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchedIdGeneratorTest {

    private BatchedIdGenerator batchIdGenerator;

    @Before
    public void initialize() {
        batchIdGenerator = new BatchedIdGenerator(Id.first());
    }

    @Test
    public void validateShortenedUrlFormat() {
        assertThat(batchIdGenerator.generate(1).ids().iterator().next()).hasSize(5);
    }

    @Test
    public void returnsUniqueUrl() {
        Batch batch = batchIdGenerator.generate(1000);

        // If all were unique then set should have all unique values
        assertThat(batch.ids()).hasSize(1000);
    }

    @Test
    public void returnsNextIdToBeGenerated() throws IdExhaustedException {
        Batch batch = batchIdGenerator.generate(1);

        assertThat(batch.nextId().encode()).isEqualTo(Id.first().next().encode());
    }
}