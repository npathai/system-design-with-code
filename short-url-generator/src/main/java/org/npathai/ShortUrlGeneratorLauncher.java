package org.npathai;

import org.npathai.zookeeper.DefaultZkManager;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import spark.Spark;

import static spark.Spark.before;

public class ShortUrlGeneratorLauncher {

    public static final int PORT = 4321;
    private Router router;
    private DefaultZkManager zkManager;

    public static void main(String[] args) throws Exception {
        ShortUrlGeneratorLauncher shortUrlGeneratorLauncher = new ShortUrlGeneratorLauncher();
        shortUrlGeneratorLauncher.start();
        shortUrlGeneratorLauncher.awaitInitialization();
        System.out.println("Press any key to exit..");
        System.in.read();
        shortUrlGeneratorLauncher.stop();
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
        DefaultZkManagerFactory zkManagerFactory = new DefaultZkManagerFactory();
        zkManager = zkManagerFactory.createConnected("0.0.0.0:2181");

        setupSpark();

        router = new Router(zkManager);
        router.initRoutes();
    }


    public void awaitInitialization() {
        Spark.awaitInitialization();
    }

    public void stop() throws Exception {
        Spark.stop();
        router.stop();
        zkManager.stop();
    }
}
