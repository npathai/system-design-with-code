package org.npathai.dao;

import org.npathai.model.AnalyticsInfo;

import java.util.Optional;

public interface AnalyticsDao {
    void incrementClick(String id);
    Optional<AnalyticsInfo> getById(String id);
}
