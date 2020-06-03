package org.npathai.discovery.zookeeper;

import org.apache.curator.x.discovery.ServiceInstance;
import org.npathai.discovery.Instance;

public class ZkInstance implements Instance {
    private ServiceInstance<String> serviceInstance;

    ZkInstance(ServiceInstance<String> serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    @Override
    public String name() {
        return serviceInstance.getName();
    }

    @Override
    public String id() {
        return serviceInstance.getId();
    }

    @Override
    public String address() {
        return serviceInstance.getAddress();
    }

    @Override
    public Integer port() {
        return serviceInstance.getPort();
    }
}
