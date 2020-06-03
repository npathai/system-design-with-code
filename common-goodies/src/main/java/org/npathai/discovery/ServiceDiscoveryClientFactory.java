package org.npathai.discovery;

import java.util.Properties;

public interface ServiceDiscoveryClientFactory {
    ServiceDiscoveryClient createDiscoveryClient(String serviceName, Properties properties) throws DiscoveryException;
}
