package org.npathai.util.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class DefaultScheduledJobService implements ScheduledJobService {

    private final ScheduledExecutorService scheduledExecutorService;

    public DefaultScheduledJobService() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return scheduledExecutorService.submit(callable);
    }
}
