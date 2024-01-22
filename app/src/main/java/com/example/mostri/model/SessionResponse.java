package com.example.mostri.model;

import com.google.gson.annotations.SerializedName;

public class SessionResponse {
    @SerializedName("sid")
    private String sessionId;

    @SerializedName("uid")
    private String userId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
