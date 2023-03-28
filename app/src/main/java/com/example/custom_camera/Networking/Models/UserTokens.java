package com.example.custom_camera.Networking.Models;

public class UserTokens {
    private String contentType;
    private String accessToken;
    private String uid;
    private String client;

    public UserTokens(String contentType, String accessToken, String uid, String client) {
        this.contentType = contentType;
        this.accessToken = accessToken;
        this.uid = uid;
        this.client = client;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getUid() {
        return uid;
    }

    public String getClient() {
        return client;
    }
}
