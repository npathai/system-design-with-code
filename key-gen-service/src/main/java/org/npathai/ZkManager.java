package org.npathai;

import org.apache.zookeeper.data.Stat;

public interface ZkManager {
    Stat createIfAbsent(String path) throws Exception;
    void runWithinLock(String lockPath, ThrowingRunnable runnable) throws Exception;
    byte[] getData(String path) throws Exception;
    void setData(String path, byte[] bytes) throws Exception;
    void stop();
}
