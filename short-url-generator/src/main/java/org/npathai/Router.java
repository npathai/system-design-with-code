package org.npathai;

import org.npathai.controller.RootController;
import org.npathai.api.UrlExpanderAPI;
import org.npathai.api.UrlShortenerAPI;
import org.npathai.dao.InMemoryUrlDao;
import org.npathai.domain.UrlShortener;
import org.npathai.service.IdGenerationService;
import spark.Spark;

public class Router {

    public static void initRoutes() {
        IdGenerationService idGenerationService = new IdGenerationService();
        UrlShortener urlShortener = new UrlShortener(idGenerationService, new InMemoryUrlDao());
        UrlShortenerAPI urlShortenerController =
                new UrlShortenerAPI(urlShortener);
        UrlExpanderAPI urlExpanderAPI =
                new UrlExpanderAPI(urlShortener);
        RootController rootController = new RootController(urlShortener);

        Spark.post("/shorten", (req, res) -> urlShortenerController.shorten(req, res));
        Spark.get("/expand/:id", (req, res) -> urlExpanderAPI.expand(req, res));


        // Unprotected paths
        Spark.get("/:id", (req, res) -> rootController.handle(req, res));
    }
}