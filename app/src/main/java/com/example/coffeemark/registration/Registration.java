package com.example.coffeemark.registration;

import com.example.coffeemark.cafe.InCafeBase;

import java.util.List;

public class Registration {
    private final String username;
    private final String password;
    private final String email;
    private final String role;
    private final List<InCafeBase> inCafeBaseList;
    private final String image;
    private final String public_key;
    private final String uuid;

    private Registration(Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.role = builder.role;
        this.inCafeBaseList = builder.inCafeBaseList;
        this.image = builder.image;
        this.public_key=builder.public_key;
        this.uuid=builder.uuid;

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

    public List<InCafeBase> getCafeBaseList() {
        return inCafeBaseList;
    }

    public String getImage() {
        return image;
    }
    public String getPublic_key(){
        return public_key;
    }
    public String getUuid(){
        return uuid;
    }
    // Builder class
    public static class Builder {
        private String username;
        private String password;
        private String email;
        private String role;
        private List<InCafeBase> inCafeBaseList;
        private String image;
        private String public_key;
        private String uuid;

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

        public Builder cafes(List<InCafeBase> inCafeBaseList) {
            this.inCafeBaseList = inCafeBaseList;
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
        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }
        public Registration build() {
            return new Registration(this);
        }
    }
}

