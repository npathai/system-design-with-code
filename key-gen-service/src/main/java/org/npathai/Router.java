package org.npathai;

import org.npathai.api.IdGeneratorAPI;
import org.npathai.domain.BatchedIdGenerator;
import org.npathai.domain.Id;
import org.npathai.domain.IdProviderService;
import org.npathai.zookeeper.DefaultZkManager;
import org.npathai.zookeeper.DefaultZkManagerFactory;
import spark.Spark;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Router {

    private DefaultZkManager manager;
    private IdProviderService idProviderService;
    private ScheduledExecutorService scheduledExecutor;

    public Router() {

    }

    public void initRoutes() throws Exception {
        DefaultZkManagerFactory zkManagerFactory = new DefaultZkManagerFactory();
        manager = zkManagerFactory.createConnectedManager("0.0.0.0:2181");
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        idProviderService = new IdProviderService(manager, scheduledExecutor);
        IdGeneratorAPI idGeneratorAPI = new IdGeneratorAPI(idProviderService);

        Spark.get("/generate", (req, res) -> idGeneratorAPI.generate(req, res));
    }

    public void stop() throws InterruptedException {
        manager.stop();
        scheduledExecutor.shutdown();
        scheduledExecutor.awaitTermination(100, TimeUnit.SECONDS);
    }
}
