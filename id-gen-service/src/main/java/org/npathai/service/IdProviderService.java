package org.npathai.service;

import org.npathai.domain.Batch;
import org.npathai.domain.BatchedIdGenerator;
import org.npathai.domain.Id;
import org.npathai.zookeeper.ZkManager;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class IdProviderService {

    public static final String NEXT_ID_ZNODE_NAME = "/next-id";
    private final ZkManager manager;
    private final ScheduledExecutorService scheduledExecutorService;
    private ConcurrentLinkedQueue<String> cachedIds = new ConcurrentLinkedQueue<>();

    // FIXME constructor is doing real work. Options?
    // FIXME rethink the decision of ScheduledExecutorService, not scheduling. Can we replace with ExecutorService?
    // FIXME because of async nature of trigger hydration, service might become available even though keys are not
    // cached. Are we okay with that?
    public IdProviderService(ZkManager manager, ScheduledExecutorService scheduledExecutorService) throws Exception {
        this.manager = manager;
        this.scheduledExecutorService = scheduledExecutorService;
        manager.createIfAbsent(NEXT_ID_ZNODE_NAME);
        triggerHydrationAsync().get();
    }

    public String nextId() {
        if (cachedIds.size() < 5) {
            triggerHydrationAsync();
        }
        return cachedIds.poll();
    }

    private Future<?> triggerHydrationAsync() {
        return scheduledExecutorService.submit(manager.withinLock(NEXT_ID_ZNODE_NAME,
                new BatchGenerationProcess(manager)));
    }

    private class BatchGenerationProcess implements Callable<Void> {
        private final ZkManager manager;

        BatchGenerationProcess(ZkManager manager) {
            this.manager = manager;
        }

        @Override
        /* TODO batch generation algorithm can be improved for performance - Lock stripping
         * 1) Acquire lock and read next id
         * 2) Increment next id by BATCH count and update zookeeper node
         * 3) Release lock
         * 4) Generate batch ids and cache them
         *
         * This will reduce the overall locking period by some amount if batch size is higher.
         */
        public Void call() throws Exception {
            System.out.println("Batch Id generation started");
            byte[] data = manager.getData(NEXT_ID_ZNODE_NAME);
            Id startId;
            if (data.length == 0) {
                System.out.println("Next id data is empty");
                startId = Id.first();
            } else {
                startId = Id.fromEncoded(new String(data));
                System.out.println("Next id data is not null. Found value: " + startId.encode());
            }
            BatchedIdGenerator generator = new BatchedIdGenerator(startId);
            Batch batch = generator.generate(10);
            cachedIds.addAll(batch.ids());
            System.out.println(cachedIds);

            manager.setData(NEXT_ID_ZNODE_NAME, batch.nextId().encode().getBytes());
            System.out.println("Saved next id value in zookeeper as: " + batch.nextId().encode());
            System.out.println("Batch Id generation ended");

            return null;
        }
    }
}
