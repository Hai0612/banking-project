package com.bank.apigateway.model;


import java.util.Collections;
import java.util.List;

public class GatewayUserContext {

    private String userId;
    private String username;
    private List<String> roles = Collections.emptyList();
    private String traceId;

    public GatewayUserContext() {}

    public GatewayUserContext(String userId, String username, List<String> roles, String traceId) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.traceId = traceId;
    }

    // getters & setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    @Override
    public String toString() {
        return "GatewayUserContext{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
