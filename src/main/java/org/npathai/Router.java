package org.npathai;

import org.npathai.controller.UrlShortnerController;
import spark.Spark;

public class Router {
    static void initRoutes() {
        UrlShortnerController urlShortnerController = new UrlShortnerController();
        Spark.post("/shorten", (req, res) -> urlShortnerController.shorten(req, res));
    }
}