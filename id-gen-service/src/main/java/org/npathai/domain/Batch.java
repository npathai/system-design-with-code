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

    public Id nextId() {
        return nextId;
    }
}
