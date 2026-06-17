package com.datahub.dto;

import com.datahub.entity.Asset;
import com.datahub.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AssetResponse {
    private Long assetId;
    private Long providerId;
    private ProviderInfo provider;
    private String title;
    private Asset.AssetType assetType;
    private String description;
    private String endpointUrl;
    private BigDecimal pricePerMonth;
    private Asset.Status status;
    private String tags;
    private LocalDateTime createdAt;

    public static AssetResponse from(Asset asset) {
        AssetResponse response = new AssetResponse();
        response.setAssetId(asset.getAssetId());
        response.setProviderId(asset.getProviderId());
        response.setTitle(asset.getTitle());
        response.setAssetType(asset.getAssetType());
        response.setDescription(asset.getDescription());
        response.setEndpointUrl(asset.getEndpointUrl());
        response.setPricePerMonth(asset.getPricePerMonth());
        response.setStatus(asset.getStatus());
        response.setTags(asset.getTags());
        response.setCreatedAt(asset.getCreatedAt());
        response.setProvider(ProviderInfo.from(asset.getProvider()));
        return response;
    }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }
    public ProviderInfo getProvider() { return provider; }
    public void setProvider(ProviderInfo provider) { this.provider = provider; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Asset.AssetType getAssetType() { return assetType; }
    public void setAssetType(Asset.AssetType assetType) { this.assetType = assetType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public Asset.Status getStatus() { return status; }
    public void setStatus(Asset.Status status) { this.status = status; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class ProviderInfo {
        private Long userId;
        private String username;
        private String avatar;

        public static ProviderInfo from(User user) {
            if (user == null) {
                return null;
            }
            ProviderInfo info = new ProviderInfo();
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
