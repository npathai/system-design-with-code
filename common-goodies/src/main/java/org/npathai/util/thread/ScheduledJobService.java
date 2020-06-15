package org.npathai.util.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ScheduledJobService {
    <T> Future<T> submit(Callable<T> callable);
}
