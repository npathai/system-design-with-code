package org.npathai;

import org.npathai.properties.ApplicationProperties;
import org.npathai.util.NullSafe;
import org.npathai.zookeeper.DefaultZkManager;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static spark.Spark.before;

public class ShortUrlGeneratorLauncher {

    public static final int PORT = 4321;
    private Router router;
    private DefaultZkManager zkManager;
    private Properties applicationProperties;

    public static void main(String[] args) throws Exception {
        ShortUrlGeneratorLauncher shortUrlGeneratorLauncher = new ShortUrlGeneratorLauncher();
        try {
            shortUrlGeneratorLauncher.start();
            shortUrlGeneratorLauncher.awaitInitialization();
            System.out.println("Press any key to exit..");
            System.in.read();
        } finally {
            shortUrlGeneratorLauncher.stop();
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
        zkManager = zkManagerFactory.createConnected(
                applicationProperties.getProperty(ApplicationProperties.ZOOKEEPER_URL.name()));

        setupSpark();

        router = new Router(applicationProperties, zkManager);
        router.initRoutes();
    }

    private void readApplicationProperties() throws IOException {
        applicationProperties = new Properties();
        applicationProperties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
    }


    public void awaitInitialization() {
        Spark.awaitInitialization();
    }

    public void stop() throws Exception {
        Spark.stop();
        NullSafe.ifNotNull(router, Router::stop);
        NullSafe.ifNotNull(zkManager, ZkManager::stop);
    }
}
