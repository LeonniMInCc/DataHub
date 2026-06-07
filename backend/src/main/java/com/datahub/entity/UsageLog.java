package com.datahub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usage_logs")
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "sub_id", nullable = false)
    private Long subId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_id", insertable = false, updatable = false)
    private Subscription subscription;

    @Column(name = "request_time", nullable = false)
    private LocalDateTime requestTime;

    @Column(name = "status_code", nullable = false)
    private Integer statusCode;

    @Column(name = "response_time", nullable = false)
    private Integer responseTime;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @PrePersist
    protected void onCreate() {
        if (requestTime == null) requestTime = LocalDateTime.now();
        if (statusCode == null) statusCode = 200;
        if (responseTime == null) responseTime = 0;
    }

    public UsageLog() {}

    // Getters and Setters
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public Long getSubId() { return subId; }
    public void setSubId(Long subId) { this.subId = subId; }
    public Subscription getSubscription() { return subscription; }
    public void setSubscription(Subscription subscription) { this.subscription = subscription; }
    public LocalDateTime getRequestTime() { return requestTime; }
    public void setRequestTime(LocalDateTime requestTime) { this.requestTime = requestTime; }
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public Integer getResponseTime() { return responseTime; }
    public void setResponseTime(Integer responseTime) { this.responseTime = responseTime; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
