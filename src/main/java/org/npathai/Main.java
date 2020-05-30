package org.npathai;

import spark.Spark;

public class Main {

    public static final int PORT = 4321;

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        main.start();
        main.awaitInitialization();
        Thread.currentThread().join();
    }

    private void setupSpark() {
        Spark.port(PORT);
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
