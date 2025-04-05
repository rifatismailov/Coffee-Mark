package com.example.coffeemark.service.authorization;

public class AuthorizationRequest {
    private final String email;
    private final String password;

    private AuthorizationRequest(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Builder class
    public static class Builder {
        private String email;
        private String password;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public AuthorizationRequest build() {
            return new AuthorizationRequest(this);
        }
    }
}

