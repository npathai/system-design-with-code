package org.npathai.util.thread;

public interface ThrowingRunnable {
    void run() throws Exception;

    default SwallowingRunnable toSwallowing() {
        return new SwallowingRunnable(this);
    }
}
