package org.npathai.domain;

import java.util.*;

/**
 * This piece will only run
 */
public class BatchedIdGenerator {
    private Id current;

    public BatchedIdGenerator(Id current) {
        this.current = current;
    }

    public synchronized Batch generate(int batchSize) {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < batchSize; i++) {
            ids.add(getAndIncrementCurrentId());
        }
        Batch batch = new Batch(ids, current);
        return batch;
    }

    private String getAndIncrementCurrentId() {
        String id = current.encode();
        current = current.next();
        return id;
    }
}