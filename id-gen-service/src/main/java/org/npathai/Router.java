package org.npathai;

import org.npathai.api.IdGeneratorAPI;
import org.npathai.domain.IdGenerationService;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Router implements Stoppable {

    private final ZkManager manager;
    private IdGenerationService idGenerationService;
    private ScheduledExecutorService scheduledExecutor;

    public Router(ZkManager manager) {
        this.manager = manager;
    }

    public void initRoutes() throws Exception {
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        idGenerationService = new IdGenerationService(manager, scheduledExecutor);
        IdGeneratorAPI idGeneratorAPI = new IdGeneratorAPI(idGenerationService);

        Spark.get("/generate", (req, res) -> idGeneratorAPI.generate(req, res));
    }

    @Override
    public void stop() throws InterruptedException {
        scheduledExecutor.shutdown();
        scheduledExecutor.awaitTermination(100, TimeUnit.SECONDS);
    }
}
