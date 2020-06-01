package org.npathai;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

public class DefaultZkManager implements ZkManager {

    private final CuratorFramework client;

    public DefaultZkManager(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public Stat createIfAbsent(String path) throws Exception {
        try {
            client.create().creatingParentContainersIfNeeded().forPath(path, "".getBytes());
        } catch (KeeperException.NodeExistsException ex) {
            System.out.println(path + " node already exists.");
        }
        return null;
    }

    @Override
    public void runWithinLock(String lockPath, ThrowingRunnable runnable) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client, lockPath);
        if (!lock.acquire(100, TimeUnit.SECONDS)) {
            throw new IllegalStateException("Could not acquire the lock to Next id node");
        }
        try {
            System.out.println("Acquired lock: " + System.currentTimeMillis());
            runnable.run();
        } finally {
            lock.release(); // always release the lock in a finally block
            System.out.println("Released lock: " + System.currentTimeMillis());
        }
    }

    @Override
    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    @Override
    public void setData(String path, byte[] bytes) throws Exception {
        client.setData().forPath(path, bytes);
    }

    @Override
    public void stop() {
        client.close();
    }
}
