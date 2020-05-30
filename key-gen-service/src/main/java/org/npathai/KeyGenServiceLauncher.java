package org.npathai;

import spark.Spark;

import static spark.Spark.before;

public class KeyGenServiceLauncher {

    public static final int PORT = 4322;

    public static void main(String[] args) throws InterruptedException {
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
