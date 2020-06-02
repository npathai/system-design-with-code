package org.npathai;

import org.npathai.controller.RootController;
import org.npathai.api.UrlExpanderAPI;
import org.npathai.api.UrlShortenerAPI;
import org.npathai.dao.InMemoryUrlDao;
import org.npathai.domain.UrlShortener;
import org.npathai.service.IdGenerationService;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.io.Closeable;
import java.io.IOException;

public class Router implements Stoppable {

    private final ZkManager zkManager;
    private IdGenerationService idGenerationService;

    public Router(ZkManager zkManager) {
        this.zkManager = zkManager;
    }

    public void initRoutes() throws Exception {
        idGenerationService = new IdGenerationService(zkManager);
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

    @Override
    public void stop() throws Exception {
        idGenerationService.stop();
    }
}