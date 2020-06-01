package org.npathai;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class DefaultZkManagerFactory {

    public DefaultZkManager createConnectedManager(String connectionString) throws InterruptedException {
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectionString,
                new ExponentialBackoffRetry(1000, 3));
        System.out.println("Connecting to Zookeeper");
        client.start();
        client.blockUntilConnected();
        System.out.println("Connected to Zookeeper");
        return new DefaultZkManager(client);
    }
}
