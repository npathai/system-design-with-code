package org.npathai;

import spark.Spark;

import static spark.Spark.before;

public class ShortUrlGeneratorLauncher {

    public static final int PORT = 4321;

    public static void main(String[] args) throws InterruptedException {
        ShortUrlGeneratorLauncher shortUrlGeneratorLauncher = new ShortUrlGeneratorLauncher();
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

    public void start() {
        setupSpark();
        Router.initRoutes();
    }


    public void awaitInitialization() {
        Spark.awaitInitialization();
    }

    public void stop() {
        Spark.stop();
    }
}
