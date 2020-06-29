package org.npathai.model;

public class AnalyticsInfo {
    private String id;
    private int clickCount;

    public AnalyticsInfo() {

    }

    public AnalyticsInfo(String id) {
        this.id = id;
        this.clickCount = 0;
    }

    public AnalyticsInfo(String id, int clickCount) {
        this.id = id;
        this.clickCount = clickCount;
    }

    public String getId() {
        return id;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public AnalyticsInfo incrementClick() {
        return new AnalyticsInfo(id, clickCount + 1);
    }
}
