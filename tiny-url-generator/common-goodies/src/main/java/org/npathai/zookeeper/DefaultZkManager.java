package org.npathai.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class DefaultZkManager implements ZkManager {
    private static final Logger LOG = LogManager.getLogger(DefaultZkManager.class);

    private final CuratorFramework client;

    public DefaultZkManager(CuratorFramework client) {
        this.client = client;
    }

    @Override
    public Stat createIfAbsent(String path) throws Exception {
        try {
            client.create().creatingParentContainersIfNeeded().forPath(path, "".getBytes());
        } catch (KeeperException.NodeExistsException ex) {
            LOG.debug(path + " node already exists.");
        }
        return null;
    }

    @Override
    public <T> Callable<T> withinLock(String lockPath, Callable<T> callable) {
        return () -> {
            InterProcessMutex lock = new InterProcessMutex(client, lockPath);
            if (!lock.acquire(100, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Could not acquire the lock to Next id node");
            }
            try {
                LOG.debug("Acquired lock: " + System.currentTimeMillis());
                return callable.call();
            } finally {
                lock.release(); // always release the lock in a finally block
                LOG.debug("Released lock: " + System.currentTimeMillis());
            }
        };
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

    @Override
    public CuratorFramework client() {
        return client;
    }
}
