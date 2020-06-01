package org.npathai;

import spark.Spark;

import static spark.Spark.before;

public class KeyGenServiceLauncher {

    public static final int PORT = 4322;
    private Router router;

    public static void main(String[] args) throws Exception {
        KeyGenServiceLauncher shortUrlGeneratorLauncher = new KeyGenServiceLauncher();
        shortUrlGeneratorLauncher.start();
        shortUrlGeneratorLauncher.awaitInitialization();
        Thread.currentThread().join();
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
        setupSpark();
        router = new Router();
        router.initRoutes();
    }


    public void awaitInitialization() {
        Spark.awaitInitialization();
    }

    public void stop() throws InterruptedException {
        Spark.stop();
        router.stop();
    }
}
