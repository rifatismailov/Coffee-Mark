package com.example.coffeemark.util;

import org.json.JSONObject;

public class LocalErrorResponse {
    private String status;
    private String message;

    // Порожній конструктор
    public LocalErrorResponse() {}

    // Конструктор для десеріалізації з JSONObject
    public LocalErrorResponse(JSONObject jsonObject) {
        try {
            this.status = jsonObject.optString("status", null);
            this.message = jsonObject.optString("message", null);
        } catch (Exception e) {
            // Можеш залогувати помилку, якщо потрібно
        }
    }

    // Геттери
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    // Сеттери
    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // ✅ Builder
    public static class Builder {
        private final LocalErrorResponse response;

        public Builder() {
            response = new LocalErrorResponse();
        }

        public Builder status(String status) {
            response.setStatus(status);
            return this;
        }

        public Builder message(String message) {
            response.setMessage(message);
            return this;
        }

        public LocalErrorResponse build() {
            return response;
        }
    }
}


