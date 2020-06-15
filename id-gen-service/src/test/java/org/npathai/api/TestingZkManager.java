package org.npathai.api;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.npathai.annotations.TestingUtil;
import org.npathai.zookeeper.ZkManager;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@Requires(env = Environment.TEST)
@Replaces(ZkManager.class)
@TestingUtil
@Singleton
public class TestingZkManager implements ZkManager {

    private Map<String, byte[]> data = new HashMap<>();

    @Override
    public Stat createIfAbsent(String path) throws Exception {
        return null;
    }

    @Override
    public <T> Callable<T> withinLock(String lockPath, Callable<T> callable) {
        return () -> callable.call();
    }


    @Override
    public byte[] getData(String path) throws Exception {
        return data.get(path);
    }

    @Override
    public void setData(String path, byte[] bytes) throws Exception {
        data.put(path, bytes);
    }

    @Override
    public CuratorFramework client() {
        return null;
    }

    @Override
    public void stop() throws Exception {

    }
}
