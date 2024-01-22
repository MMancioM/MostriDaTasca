package com.example.mostri.api;

import com.google.gson.annotations.SerializedName;

public class ActivationRequest {
    @SerializedName("sid")
    private String sid;

    public ActivationRequest(String sid) {
        this.sid = sid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
