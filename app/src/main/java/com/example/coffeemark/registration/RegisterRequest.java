package com.example.coffeemark.registration;

import com.example.coffeemark.registration.cafe.Cafe;

import java.util.List;

public class RegisterRequest {
    private final String username;
    private final String password;
    private final String email;
    private final String role;
    private final List<Cafe> cafes;  // Змінив з cafeList на cafes
    private final String cafeImage;

    public RegisterRequest(String username, String password, String email, String role, List<Cafe> cafes, String cafeImage) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.cafes = cafes;
        this.cafeImage=cafeImage;
    }

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

    public List<Cafe> getCafes() {  // Змінив з getCafeList на getCafes
        return cafes;
    }

    public String getCafeImage() {
        return cafeImage;
    }
}
