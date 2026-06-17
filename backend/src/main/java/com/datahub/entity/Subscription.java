package com.datahub.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(name = "uk_sub_asset_user", columnNames = {"subscriber_id", "asset_id"}))
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    private Long subId;

    @Column(name = "subscriber_id", nullable = false)
    private Long subscriberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User subscriber;

    @Column(name = "asset_id", nullable = false)
    private Long assetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", insertable = false, updatable = false)
    private Asset asset;

    @Column(name = "quota_limit", nullable = false)
    private Integer quotaLimit;

    @Column(name = "quota_used", nullable = false)
    private Integer quotaUsed;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (quotaUsed == null) quotaUsed = 0;
        if (quotaLimit == null) quotaLimit = 1000;
        if (status == null) status = SubStatus.PENDING_PAYMENT;
    }

    public Subscription() {}

    public enum SubStatus { PENDING_PAYMENT, ACTIVE, FAILED, EXPIRED, CANCELLED }

    // Getters and Setters
    public Long getSubId() { return subId; }
    public void setSubId(Long subId) { this.subId = subId; }
    public Long getSubscriberId() { return subscriberId; }
    public void setSubscriberId(Long subscriberId) { this.subscriberId = subscriberId; }
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }
    public Integer getQuotaLimit() { return quotaLimit; }
    public void setQuotaLimit(Integer quotaLimit) { this.quotaLimit = quotaLimit; }
    public Integer getQuotaUsed() { return quotaUsed; }
    public void setQuotaUsed(Integer quotaUsed) { this.quotaUsed = quotaUsed; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public SubStatus getStatus() { return status; }
    public void setStatus(SubStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
