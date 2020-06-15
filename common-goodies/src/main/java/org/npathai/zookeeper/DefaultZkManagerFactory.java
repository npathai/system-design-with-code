package org.npathai.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class DefaultZkManagerFactory {
    private static final Logger LOG = LogManager.getLogger(DefaultZkManagerFactory.class);

    public DefaultZkManager createConnected(String connectionString) throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString,
                new ExponentialBackoffRetry(1000, 3));
        LOG.info("Connecting to Zookeeper using connection string: {}", connectionString);
        client.start();
        LOG.info("Connected to Zookeeper");
        return new DefaultZkManager(client);
    }
}
