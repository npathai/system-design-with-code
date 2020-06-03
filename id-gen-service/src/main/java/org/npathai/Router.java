package org.npathai;

import org.npathai.api.IdGeneratorAPI;
import org.npathai.service.IdProviderService;
import org.npathai.util.Stoppable;
import org.npathai.zookeeper.ZkManager;
import spark.Spark;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Router implements Stoppable {

    private final ZkManager manager;
    private IdProviderService idProviderService;
    private ScheduledExecutorService scheduledExecutor;

    public Router(ZkManager manager) {
        this.manager = manager;
    }

    public void initRoutes() throws Exception {
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        idProviderService = new IdProviderService(manager, scheduledExecutor);
        IdGeneratorAPI idGeneratorAPI = new IdGeneratorAPI(idProviderService);

        Spark.get("/generate", (req, res) -> idGeneratorAPI.generate(req, res));
    }

    @Override
    public void stop() throws InterruptedException {
        scheduledExecutor.shutdown();
        scheduledExecutor.awaitTermination(100, TimeUnit.SECONDS);
    }
}
