package com.example.coffeemark.service.public_key;

import com.example.coffeemark.service.authorization.AuthorizationRequest;

public class LocalPublicKeyRequest {
    private AuthorizationRequest request;
    private String key;

    public LocalPublicKeyRequest(AuthorizationRequest request, String key) {
        this.request = request;
        this.key = key;
    }

    public AuthorizationRequest getRequest() {
        return request;
    }

    public void setRequest(AuthorizationRequest request) {
        this.request = request;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

