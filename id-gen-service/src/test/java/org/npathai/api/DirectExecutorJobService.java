package org.npathai.api;

import com.google.common.util.concurrent.MoreExecutors;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.npathai.util.thread.ScheduledJobService;
import org.npathai.annotations.TestingUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Requires(env = Environment.TEST)
@Replaces(ScheduledJobService.class)
@TestingUtil
@SuppressWarnings("unused")
public class DirectExecutorJobService implements ScheduledJobService {
    private ExecutorService executorService = MoreExecutors.newDirectExecutorService();

    @Override
    public <T> Future<T> submit(Callable<T> callable) {
        return executorService.submit(callable);
    }
}
