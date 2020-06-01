package org.npathai.util.thread;

public class SwallowingRunnable implements Runnable{

    private final ThrowingRunnable throwingRunnable;

    public SwallowingRunnable(ThrowingRunnable throwingRunnable) {
        this.throwingRunnable = throwingRunnable;
    }

    @Override
    public void run() {
        try {
            throwingRunnable.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
