package com.example.coffeemark.service.public_key;

public class PublicKeyRequest {
    // Ти можеш передавати якусь інформацію в запиті, наприклад, ідентифікатор
    private String hash;

    public PublicKeyRequest(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}

