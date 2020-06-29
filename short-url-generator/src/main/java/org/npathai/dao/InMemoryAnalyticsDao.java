package org.npathai.dao;

import org.npathai.model.AnalyticsInfo;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAnalyticsDao implements AnalyticsDao {

    private Map<String, AnalyticsInfo> clickCount = new ConcurrentHashMap<>();

    @Override
    public void incrementClick(String id) {
        clickCount.putIfAbsent(id, new AnalyticsInfo(id));
        clickCount.computeIfPresent(id, (key, oldVal) -> oldVal.incrementClick());
    }

    @Override
    public Optional<AnalyticsInfo> getById(String id) {
        return Optional.ofNullable(clickCount.get(id));
    }
}