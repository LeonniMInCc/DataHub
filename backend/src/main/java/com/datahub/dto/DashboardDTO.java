package com.datahub.dto;

import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private long totalUsers;
    private long totalAssets;
    private long totalSubscriptions;
    private long totalApiCalls;
    private List<Map<String, Object>> assetTypeDistribution;
    private List<Map<String, Object>> last7DaysCalls;
    private List<Map<String, Object>> top5Assets;

    public DashboardDTO() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalAssets() { return totalAssets; }
    public void setTotalAssets(long totalAssets) { this.totalAssets = totalAssets; }
    public long getTotalSubscriptions() { return totalSubscriptions; }
    public void setTotalSubscriptions(long totalSubscriptions) { this.totalSubscriptions = totalSubscriptions; }
    public long getTotalApiCalls() { return totalApiCalls; }
    public void setTotalApiCalls(long totalApiCalls) { this.totalApiCalls = totalApiCalls; }
    public List<Map<String, Object>> getAssetTypeDistribution() { return assetTypeDistribution; }
    public void setAssetTypeDistribution(List<Map<String, Object>> assetTypeDistribution) { this.assetTypeDistribution = assetTypeDistribution; }
    public List<Map<String, Object>> getLast7DaysCalls() { return last7DaysCalls; }
    public void setLast7DaysCalls(List<Map<String, Object>> last7DaysCalls) { this.last7DaysCalls = last7DaysCalls; }
    public List<Map<String, Object>> getTop5Assets() { return top5Assets; }
    public void setTop5Assets(List<Map<String, Object>> top5Assets) { this.top5Assets = top5Assets; }
}
