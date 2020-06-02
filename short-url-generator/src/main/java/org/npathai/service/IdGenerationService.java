package org.npathai.service;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.Closeable;
import java.io.IOException;

public class IdGenerationService implements Stoppable {
    private static final String INSTANCES_ZNODE = "/instances";
    private final ZkManager zkManager;
    private final ServiceDiscovery<String> serviceDiscovery;

    private Client client = ClientBuilder.newClient();
    private final ServiceProvider<String> serviceProvider;

    public IdGenerationService(ZkManager zkManager) throws Exception {
        this.zkManager = zkManager;
        serviceDiscovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(zkManager.client())
                .basePath(INSTANCES_ZNODE)
                .serializer(new JsonInstanceSerializer<>(String.class))
                .build();
        serviceDiscovery.start();

        serviceProvider = serviceDiscovery.serviceProviderBuilder()
                .serviceName("key-gen-service")
                .build();
        serviceProvider.start();
    }

    public String getId() throws Exception {
        try {
            ServiceInstance<String> chosenInstance = chooseInstanceExplosively();

            System.out.println("Getting key from key-gen-service instance: " + chosenInstance.getAddress());
            String response = client.target(chosenInstance.getAddress() + "/generate")
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);

            JsonObject responseJson = Json.parse(response).asObject();

            return responseJson.get("id").asString();
        } catch (ProcessingException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ServiceInstance<String> chooseInstanceExplosively() throws Exception {
        ServiceInstance<String> chosenInstance = serviceProvider.getInstance();
        if (chosenInstance == null) {
            throw new Exception("No key-gen-service instance available");
        }
        return chosenInstance;
    }


    @Override
    public void stop() throws IOException {
        serviceProvider.close();
        serviceDiscovery.close();
    }
}
