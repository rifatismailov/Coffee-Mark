package com.example.coffeemark.service.public_key;

public class PublicKeyRequest {
    // Ти можеш передавати якусь інформацію в запиті, наприклад, ідентифікатор
    private String request_body;

    public PublicKeyRequest(String request_body) {
        this.request_body = request_body;
    }

    public String getUserId() {
        return request_body;
    }

    public void setUserId(String request_body) {
        this.request_body = request_body;
    }
}

