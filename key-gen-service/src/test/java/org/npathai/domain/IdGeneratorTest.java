package org.npathai.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IdGeneratorTest {

    private IdGenerator generator;

    @Before
    public void initialize() {
        generator = new IdGenerator();
    }

    @Test
    public void validateShortenedUrlFormat() {
        assertThat(generator.generate()).hasSize(5);
    }

    @Test
    public void returnsUniqueUrl() {
        Set<String> generatedIds = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            generatedIds.add(generator.generate());
        }

        // If all were unique then set should have all unique values
        assertThat(generatedIds).hasSize(1000);
    }
}