package org.npathai.domain;

import java.util.Set;

public class Batch {
    private final Set<String> ids;
    private final Id nextId;

    public Batch(Set<String> ids, Id nextId) {
        this.ids = ids;
        this.nextId = nextId;
    }

    public Set<String> ids() {
        return ids;
    }

    // TODO can remove this method, now that we have provided incrementAndGet in Id
    public Id nextId() {
        return nextId;
    }
}
