package com.datahub.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId;

    @Column(name = "provider_id", nullable = false)
    private Long providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", insertable = false, updatable = false)
    private User provider;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false)
    private AssetType assetType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "endpoint_url", length = 500)
    private String endpointUrl;

    @Column(name = "price_per_month", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerMonth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(length = 500)
    private String tags;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = Status.ACTIVE;
        if (pricePerMonth == null) pricePerMonth = BigDecimal.ZERO;
    }

    public Asset() {}

    public enum AssetType { DATASET, API }
    public enum Status { ACTIVE, INACTIVE, DELETED }

    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }
    public User getProvider() { return provider; }
    public void setProvider(User provider) {
        this.provider = provider;
        this.providerId = provider == null ? null : provider.getUserId();
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public AssetType getAssetType() { return assetType; }
    public void setAssetType(AssetType assetType) { this.assetType = assetType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEndpointUrl() { return endpointUrl; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
