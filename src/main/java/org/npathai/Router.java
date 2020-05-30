package org.npathai;

import org.npathai.controller.UrlShortnerController;
import org.npathai.domain.UrlShortener;
import spark.Spark;

public class Router {

    public static void initRoutes() {
        UrlShortnerController urlShortnerController = new UrlShortnerController(new UrlShortener());
        Spark.post("/shorten", (req, res) -> urlShortnerController.shorten(req, res));
    }
}