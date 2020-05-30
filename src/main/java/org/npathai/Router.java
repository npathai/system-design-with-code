package org.npathai;

import org.npathai.controller.UrlExpanderController;
import org.npathai.controller.UrlShortnerController;
import org.npathai.dao.InMemoryUrlDao;
import org.npathai.domain.UrlShortener;
import spark.Spark;

public class Router {

    public static void initRoutes() {
        UrlShortener urlShortener = new UrlShortener(new InMemoryUrlDao());
        UrlShortnerController urlShortnerController =
                new UrlShortnerController(urlShortener);
        UrlExpanderController urlExpanderController =
                new UrlExpanderController(urlShortener);

        Spark.post("/shorten", (req, res) -> urlShortnerController.shorten(req, res));
        Spark.get("/expand/:id", (req, res) -> urlExpanderController.expand(req, res));
    }
}