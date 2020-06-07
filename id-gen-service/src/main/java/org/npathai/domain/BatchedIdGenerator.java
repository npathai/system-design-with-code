package org.npathai.domain;

import java.util.*;

/**
 * Generates batch of ids
 */
public class BatchedIdGenerator {
    private Id current;

    public BatchedIdGenerator(Id current) {
        this.current = current;
    }

    public Batch generate(int batchSize) {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < batchSize; i++) {
            try {
                ids.add(getAndIncrementCurrentId());
            } catch (IdExhaustedException e) {
                // TODO handle this logically
                e.printStackTrace();
            }
        }
        Batch batch = new Batch(ids, current);
        return batch;
    }

    private String getAndIncrementCurrentId() throws IdExhaustedException {
        String id = current.encode();
        current = current.next();
        return id;
    }
}