package org.npathai.dao;

import org.npathai.model.AnalyticsInfo;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// TODO contemplate using time series database for providing day wise, month wise etc statistics
public class InMemoryAnalyticsDao implements AnalyticsDao {

    private Map<String, AnalyticsInfo> clickCount = new ConcurrentHashMap<>();

    @Override
    public void incrementClick(String id) {
        clickCount.computeIfPresent(id, (key, oldVal) -> oldVal.incrementClick());
    }

    @Override
    public Optional<AnalyticsInfo> getById(String id) {
        return Optional.ofNullable(clickCount.get(id));
    }

    @Override
    public void save(AnalyticsInfo analyticsInfo) {
        clickCount.put(analyticsInfo.getId(), analyticsInfo);
    }
}
