package org.npathai.domain;

import java.util.Set;

public class Batch {
    private final Set<String> ids;

    public Batch(Set<String> ids) {
        this.ids = ids;
    }

    public Set<String> ids() {
        return ids;
    }
}
