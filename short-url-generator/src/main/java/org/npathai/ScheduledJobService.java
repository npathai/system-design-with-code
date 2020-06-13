package org.npathai;

import javax.inject.Singleton;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

@Singleton
public class ScheduledJobService {

    private final ScheduledExecutorService scheduledExecutorService;

    public ScheduledJobService() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return scheduledExecutorService.submit(callable);
    }
}
