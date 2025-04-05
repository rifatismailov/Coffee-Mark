package com.example.coffeemark.service.authorization;

public class AuthorizationResponse {
    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
