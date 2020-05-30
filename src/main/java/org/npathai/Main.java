package org.npathai;

import spark.Spark;

public class Main {

    public static void main(String[] args) {
        setupSpark();
        Router.initRoutes();
    }

    private static void setupSpark() {
        Spark.port(9090);
    }
}
