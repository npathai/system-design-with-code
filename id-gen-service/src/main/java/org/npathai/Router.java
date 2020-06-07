package org.npathai;

import org.npathai.api.IdGeneratorAPI;
import org.npathai.domain.IdGenerationService;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Router implements Stoppable {

    private final ZkManager manager;
    private IdGenerationService idGenerationService;
    private ExecutorService executorService;

    public Router(ZkManager manager) {
        this.manager = manager;
    }

    public void initRoutes() throws Exception {
        executorService = Executors.newSingleThreadScheduledExecutor();
        idGenerationService = new IdGenerationService(manager, executorService);
        IdGeneratorAPI idGeneratorAPI = new IdGeneratorAPI(idGenerationService);

        Spark.get("/generate", (req, res) -> idGeneratorAPI.generate(req, res));
    }

    @Override
    public void stop() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(100, TimeUnit.SECONDS);
    }
}
