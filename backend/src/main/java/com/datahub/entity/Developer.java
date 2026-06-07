package com.datahub.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "developers")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dev_id")
    private Long devId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "api_key", unique = true, length = 64)
    private String apiKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(length = 500)
    private String avatar;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Developer() {}

    public Developer(Long devId, String username, String passwordHash, String email,
                     String apiKey, Role role, String avatar, LocalDateTime createdAt) {
        this.devId = devId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.apiKey = apiKey;
        this.role = role;
        this.avatar = avatar;
        this.createdAt = createdAt;
    }

    public enum Role {
        PROVIDER, SUBSCRIBER
    }

    // Getters and Setters
    public Long getDevId() { return devId; }
    public void setDevId(Long devId) { this.devId = devId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
