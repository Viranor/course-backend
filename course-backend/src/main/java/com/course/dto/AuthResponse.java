package com.course.dto;

public class AuthResponse {
    private String token;
    private String username;
    private Long userId;
    private String role;
    private String message;
    private boolean success = true;

    public AuthResponse() {}

    public AuthResponse(String token, String username, Long userId, String role) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.role = role;
    }

    // getter和setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}