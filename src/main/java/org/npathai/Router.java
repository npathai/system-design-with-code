package org.npathai;

import org.npathai.controller.RootController;
import org.npathai.controller.UrlExpanderAPI;
import org.npathai.controller.UrlShortenerAPI;
import org.npathai.dao.InMemoryUrlDao;
import org.npathai.domain.UrlShortener;
import spark.Spark;

public class Router {

    public static void initRoutes() {
        UrlShortener urlShortener = new UrlShortener(new InMemoryUrlDao());
        UrlShortenerAPI urlShortnerController =
                new UrlShortenerAPI(urlShortener);
        UrlExpanderAPI urlExpanderAPI =
                new UrlExpanderAPI(urlShortener);
        RootController rootController = new RootController(urlShortener);

        Spark.post("/shorten", (req, res) -> urlShortnerController.shorten(req, res));
        Spark.get("/expand/:id", (req, res) -> urlExpanderAPI.expand(req, res));


        // Unprotected paths
        Spark.get("/:id", (req, res) -> rootController.handle(req, res));
    }
}