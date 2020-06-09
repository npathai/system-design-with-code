package org.npathai.client;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.curator.x.discovery.*;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.npathai.discovery.Instance;
import org.npathai.discovery.ServiceDiscoveryClient;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class IdGenerationServiceClient implements Stoppable {
    private final ServiceDiscoveryClient serviceDiscoveryClient;
    private Client client = ClientBuilder.newClient();

    public IdGenerationServiceClient(ServiceDiscoveryClient idGenServiceDiscoveryClient) {
        this.serviceDiscoveryClient = idGenServiceDiscoveryClient;

    }

    public String getId() throws Exception {
        try {
            Instance idGenService = serviceDiscoveryClient.getInstance();

            System.out.println("Getting key from id-gen-service instance: " + idGenService.address());
            String response = client.target(idGenService.address() + "/generate")
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

    @Override
    public void stop() throws IOException {
        serviceDiscoveryClient.close();
    }
}
