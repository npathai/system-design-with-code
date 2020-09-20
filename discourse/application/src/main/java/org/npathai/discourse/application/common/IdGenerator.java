package org.npathai.discourse.application.common;

import java.util.UUID;

public class IdGenerator {

    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
