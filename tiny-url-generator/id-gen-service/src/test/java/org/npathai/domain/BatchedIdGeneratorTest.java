package org.npathai.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BatchedIdGeneratorTest {

    private BatchedIdGenerator batchIdGenerator;

    @BeforeEach
    public void initialize() {
        batchIdGenerator = new BatchedIdGenerator(Id.first());
    }

    @Test
    public void validateIdFormat() {
        assertThat(batchIdGenerator.generate(1).ids().iterator().next()).hasSize(5);
    }

    @Test
    public void returnsBatchWithUniqueIds() {
        Batch batch = batchIdGenerator.generate(1000);

        // If all were unique then set should have all unique values
        assertThat(batch.ids()).hasSize(1000);
    }
}