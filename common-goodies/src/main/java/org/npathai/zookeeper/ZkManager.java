package org.npathai.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.npathai.util.Stoppable;
import org.npathai.util.thread.ThrowingRunnable;

import java.util.concurrent.Callable;

public interface ZkManager extends Stoppable {
    Stat createIfAbsent(String path) throws Exception;
    <T> Callable<T> withinLock(String lockPath, Callable<T> callable);
    byte[] getData(String path) throws Exception;
    void setData(String path, byte[] bytes) throws Exception;
    CuratorFramework client();
}
