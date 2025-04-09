package com.example.coffeemark.registration;

import com.example.coffeemark.account.Account;
import com.example.coffeemark.registration.cafe.Cafe;

import java.util.List;

public class Registration {
    private final String username;
    private final String password;
    private final String email;
    private final String role;
    private final List<Cafe> cafes;
    private final String image;
    private final String public_key;
    private Registration(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.role = builder.role;
        this.cafes = builder.cafes;
        this.image = builder.image;
        this.public_key=builder.public_key;
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

    public List<Cafe> getCafes() {
        return cafes;
    }

    public String getImage() {
        return image;
    }
    public String getPublic_key(){
        return public_key;
    }

    // Builder class
    public static class Builder {
        private String username;
        private String password;
        private String email;
        private String role;
        private List<Cafe> cafes;
        private String image;
        private String public_key;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder cafes(List<Cafe> cafes) {
            this.cafes = cafes;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }
        public Builder public_key(String public_key) {
            this.public_key = public_key;
            return this;
        }

        public Registration build() {
            return new Registration(this);
        }
    }
}

