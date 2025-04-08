package com.example.coffeemark.authorization;


import android.util.Log;

import org.json.JSONObject;

public class Respond {
    private String username;
    private String password;
    private String email;
    private String role;
    private String image;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getImage() {
        return image;
    }

    // Конструктор для десеріалізації з JSONObject
    public Respond(JSONObject jsonObject) {
        try {
            this.username = jsonObject.optString("username", null);
            this.password = jsonObject.optString("password", null);
            this.email = jsonObject.optString("email", null);
            this.role = jsonObject.optString("role", null);
            this.image = jsonObject.optString("image", null);
        } catch (Exception e) {
            Log.e("Envelope", "помилка під час отримання JSON: " + e);
        }
    }
}
