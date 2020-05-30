package org.npathai;

import spark.Spark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static spark.Spark.before;

public class Main {

    public static final int PORT = 4321;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        main.start();
        main.awaitInitialization();
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
