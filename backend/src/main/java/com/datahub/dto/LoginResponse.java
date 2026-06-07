package com.datahub.dto;

public class LoginResponse {
    private String token;
    private Long devId;
    private String username;
    private String email;
    private String role;
    private String avatar;

    public LoginResponse() {}

    public LoginResponse(String token, Long devId, String username, String email, String role, String avatar) {
        this.token = token;
        this.devId = devId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.avatar = avatar;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Long getDevId() { return devId; }
    public void setDevId(Long devId) { this.devId = devId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}
