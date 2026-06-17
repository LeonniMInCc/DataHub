package com.datahub.dto;

import com.datahub.entity.Subscription;
import com.datahub.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SubscriptionResponse {
    private Long subId;
    private Long subscriberId;
    private UserInfo subscriber;
    private Long assetId;
    private AssetResponse asset;
    private Integer quotaLimit;
    private Integer quotaUsed;
    private LocalDate startDate;
    private LocalDate endDate;
    private Subscription.SubStatus status;
    private LocalDateTime createdAt;

    public static SubscriptionResponse from(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setSubId(subscription.getSubId());
        response.setSubscriberId(subscription.getSubscriberId());
        response.setSubscriber(UserInfo.from(subscription.getSubscriber()));
        response.setAssetId(subscription.getAssetId());
        response.setAsset(subscription.getAsset() == null ? null : AssetResponse.from(subscription.getAsset()));
        response.setQuotaLimit(subscription.getQuotaLimit());
        response.setQuotaUsed(subscription.getQuotaUsed());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        response.setStatus(subscription.getStatus());
        response.setCreatedAt(subscription.getCreatedAt());
        return response;
    }

    public Long getSubId() { return subId; }
    public void setSubId(Long subId) { this.subId = subId; }
    public Long getSubscriberId() { return subscriberId; }
    public void setSubscriberId(Long subscriberId) { this.subscriberId = subscriberId; }
    public UserInfo getSubscriber() { return subscriber; }
    public void setSubscriber(UserInfo subscriber) { this.subscriber = subscriber; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public AssetResponse getAsset() { return asset; }
    public void setAsset(AssetResponse asset) { this.asset = asset; }
    public Integer getQuotaLimit() { return quotaLimit; }
    public void setQuotaLimit(Integer quotaLimit) { this.quotaLimit = quotaLimit; }
    public Integer getQuotaUsed() { return quotaUsed; }
    public void setQuotaUsed(Integer quotaUsed) { this.quotaUsed = quotaUsed; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Subscription.SubStatus getStatus() { return status; }
    public void setStatus(Subscription.SubStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class UserInfo {
        private Long userId;
        private String username;
        private String avatar;

        public static UserInfo from(User user) {
            if (user == null) {
                return null;
            }
            UserInfo info = new UserInfo();
            info.setUserId(user.getUserId());
            info.setUsername(user.getUsername());
            info.setAvatar(user.getAvatar());
            return info;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
    }
}
