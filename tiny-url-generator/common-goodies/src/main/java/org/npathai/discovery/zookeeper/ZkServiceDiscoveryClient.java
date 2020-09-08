package org.npathai.discovery.zookeeper;

import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceProvider;
import org.npathai.discovery.DiscoveryException;
import org.npathai.discovery.Instance;
import org.npathai.discovery.ServiceDiscoveryClient;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ZkServiceDiscoveryClient implements ServiceDiscoveryClient {

    private final String serviceName;
    private final ServiceDiscovery<String> serviceDiscovery;
    private final ServiceProvider<String> serviceProvider;

    public ZkServiceDiscoveryClient(String serviceName,
                                    ServiceDiscovery<String> serviceDiscovery,
                                    ServiceProvider<String> serviceProvider) {
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
        this.serviceProvider = serviceProvider;
    }

    @Nonnull
    @Override
    public Instance getInstance() throws DiscoveryException {
        try {
            ServiceInstance<String> chosenInstance = serviceProvider.getInstance();
            if (chosenInstance == null) {
                throw new DiscoveryException("No instance is available for service: " + serviceName);
            }
            return new ZkInstance(chosenInstance);
        } catch (Exception e) {
            throw new DiscoveryException(e);
        }
    }

    @Override
    public void close() throws IOException {
        serviceProvider.close();
        serviceDiscovery.close();
    }
}
