package org.npathai.domain;

import org.npathai.util.thread.ThrowingRunnable;
import org.npathai.zookeeper.ZkManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;

public class IdProviderService {

    public static final String NEXT_ID_ZNODE_NAME = "/next-id";
    private final ZkManager manager;
    private final ScheduledExecutorService scheduledExecutorService;
    private ConcurrentLinkedQueue<String> cachedIds = new ConcurrentLinkedQueue<>();

    public IdProviderService(ZkManager manager, ScheduledExecutorService scheduledExecutorService) throws Exception {
        this.manager = manager;
        this.scheduledExecutorService = scheduledExecutorService;
        manager.createIfAbsent(NEXT_ID_ZNODE_NAME);
        manager.runWithinLock(NEXT_ID_ZNODE_NAME, new BatchGenerationProcess(manager));
    }

    public String nextId() {
        if (cachedIds.size() < 5) {
            triggerHydration();
        }
        return cachedIds.poll();
    }

    private void triggerHydration() {
        scheduledExecutorService.execute(new BatchGenerationProcess(manager).toSwallowing());
    }

    private class BatchGenerationProcess implements ThrowingRunnable {
        private final ZkManager manager;

        BatchGenerationProcess(ZkManager manager) {
            this.manager = manager;
        }

        @Override
        public void run() throws Exception {
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
        }
    }
}
