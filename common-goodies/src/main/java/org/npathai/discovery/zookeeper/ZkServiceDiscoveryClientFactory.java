package org.npathai.discovery.zookeeper;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.npathai.discovery.DiscoveryException;
import org.npathai.discovery.ServiceDiscoveryClient;
import org.npathai.discovery.ServiceDiscoveryClientFactory;
import org.npathai.zookeeper.ZkManager;

import java.util.Objects;
import java.util.Properties;

public class ZkServiceDiscoveryClientFactory implements ServiceDiscoveryClientFactory {
    public static final String Z_NODE_PATH = "zNodePath";

    private final ZkManager zkManager;

    public ZkServiceDiscoveryClientFactory(ZkManager zkManager) {
        this.zkManager = zkManager;
    }

    @Override
    public ServiceDiscoveryClient createDiscoveryClient(String serviceName, Properties properties) throws DiscoveryException {
        String zNodePath = Objects.requireNonNull(properties.getProperty(Z_NODE_PATH));
        try {
            ServiceDiscovery<String> serviceDiscovery = createServiceDiscovery(zNodePath);
            ServiceProvider<String> serviceProvider = createServiceProvider(serviceName, serviceDiscovery);
            return new ZkServiceDiscoveryClient(serviceName, serviceDiscovery, serviceProvider);
        } catch (Exception ex) {
            throw new DiscoveryException(ex);
        }
    }

    private ServiceProvider<String> createServiceProvider(String serviceName, ServiceDiscovery<String> serviceDiscovery) throws Exception {
        ServiceProvider<String> serviceProvider = serviceDiscovery.serviceProviderBuilder()
                .serviceName(serviceName)
                .build();
        serviceProvider.start();
        return serviceProvider;
    }

    private ServiceDiscovery<String> createServiceDiscovery(String zNodePath) throws Exception {
        ServiceDiscovery<String> serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(zkManager.client())
                .basePath(zNodePath)
                .serializer(new JsonInstanceSerializer<>(String.class))
                .build();
        serviceDiscovery.start();
        return serviceDiscovery;
    }
}
