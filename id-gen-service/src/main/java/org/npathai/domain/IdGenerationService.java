package org.npathai.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.npathai.util.thread.ScheduledJobService;
import org.npathai.zookeeper.ZkManager;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

public class IdGenerationService {
    private static final Logger LOG = LogManager.getLogger(IdGenerationService.class);

    public static final String NEXT_ID_ZNODE_NAME = "/next-id";
    private final ZkManager manager;
    private final ScheduledJobService scheduledJobService;
    private ConcurrentLinkedQueue<String> cachedIds = new ConcurrentLinkedQueue<>();

    // FIXME constructor is doing real work. Options?
    public IdGenerationService(ZkManager manager, ScheduledJobService scheduledJobService) throws Exception {
        this.manager = manager;
        this.scheduledJobService = scheduledJobService;
        manager.createIfAbsent(NEXT_ID_ZNODE_NAME);
        triggerHydrationAsync().get();
    }

    public String nextId() {
        String id = cachedIds.poll();
        if (cachedIds.size() < 5) {
            triggerHydrationAsync();
        }
        return id;
    }

    private Future<?> triggerHydrationAsync() {
        return scheduledJobService.submit(new BatchGenerationProcess(manager));
    }

    private class BatchGenerationProcess implements Callable<Void> {
        public static final int BATCH_SIZE = 10;
        private final ZkManager zkManager;

        BatchGenerationProcess(ZkManager zkManager) {
            this.zkManager = zkManager;
        }

        @Override
        public Void call() throws Exception {
            LOG.info("Batch Id generation started");

            Id startId = manager.withinLock(NEXT_ID_ZNODE_NAME, new GetAndSetIncrementIdOperation()).call();
            BatchedIdGenerator generator = new BatchedIdGenerator(startId);
            Batch batch = generator.generate(BATCH_SIZE);
            cachedIds.addAll(batch.ids());

            LOG.info("Batch Id generation ended. Cached size: {}", cachedIds.size());
            return null;
        }

        private class GetAndSetIncrementIdOperation implements Callable<Id> {

            @Override
            public Id call() throws Exception {
                Id startId = readStartId();
                Id nextAvailableId = startId.incrementAndGet(BATCH_SIZE);
                zkManager.setData(NEXT_ID_ZNODE_NAME, nextAvailableId.encode().getBytes());
                LOG.debug("Saved next id value in zookeeper as: " + nextAvailableId.encode());
                return startId;
            }

            private Id readStartId() throws Exception {
                byte[] data = zkManager.getData(NEXT_ID_ZNODE_NAME);
                Id startId;
                if (data.length == 0) {
                    LOG.debug("Next id data is empty");
                    startId = Id.first();
                } else {
                    startId = Id.fromEncoded(new String(data));
                    LOG.debug("Next id data is not null. Found value: {}", startId.encode());
                }
                return startId;
            }
        }
    }
}
