package org.npathai;

import org.apache.curator.x.discovery.*;
import org.npathai.properties.ApplicationProperties;
import org.npathai.util.NullSafe;
import org.npathai.zookeeper.DefaultZkManager;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import static spark.Spark.before;

public class IdGenServiceLauncher {

    public static final int PORT = new Random()
            .ints(4300, 4500)
            .findFirst()
            .getAsInt();

    private static final String INSTANCES_ZNODE = "/instances";

    private DefaultZkManager manager;
    private Router router;
    private ServiceInstance<String> instance;
    private ServiceDiscovery<String> discovery;
    private Properties applicationProperties;


    public static void main(String[] args) throws Exception {
        IdGenServiceLauncher idGenServiceLauncher = new IdGenServiceLauncher();
        try {
            idGenServiceLauncher.start();
            idGenServiceLauncher.awaitInitialization();
            System.out.println("Press any key to exit..");
            System.in.read();
        } finally {
            idGenServiceLauncher.stop();
        }
    }

    private void setupSpark() {
        Spark.port(PORT);
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");
        });
    }

    public void start() throws Exception {
        readApplicationProperties();

        DefaultZkManagerFactory zkManagerFactory = new DefaultZkManagerFactory();
        manager = zkManagerFactory.createConnected(ApplicationProperties.ZOOKEEPER_URL);

        setupSpark();
        router = new Router(manager);
        router.initRoutes();
        // On successful start
        registerForDiscovery();
        System.out.println("Successfully started listening on port: " + PORT);
    }

    private void readApplicationProperties() throws IOException {
        applicationProperties = new Properties();
        applicationProperties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
    }

    private void registerForDiscovery() throws Exception {
        UriSpec uriSpec = new UriSpec("http://localhost:{port}");
        instance = ServiceInstance.<String>builder()
                .name("id-gen-service")
                .address("http://localhost:" + PORT)
                .payload("Provides unique ids")
                .port(PORT)
                .uriSpec(uriSpec)
                .build();

        discovery = ServiceDiscoveryBuilder.builder(String.class)
                .client(manager.client())
                .basePath(INSTANCES_ZNODE)
                .thisInstance(instance)
                .build();

        discovery.start();
    }


    public void awaitInitialization() {
        Spark.awaitInitialization();
    }

    public void stop() throws Exception {
        Spark.stop();
        NullSafe.ifNotNull(router, Router::stop);
        NullSafe.ifNotNull(discovery, ServiceDiscovery::close);
        NullSafe.ifNotNull(manager, ZkManager::stop);
    }
}
