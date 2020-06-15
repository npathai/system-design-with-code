package org.npathai;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ScheduledJobService {
    <T> Future<T> submit(Callable<T> callable);
}
