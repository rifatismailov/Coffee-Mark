package com.example.coffeemark.service.authorization;

public class AuthorizationRequest {
    private final String email;
    private final String password;
    private final String hash_user_public;
    private final String uuid;

    private AuthorizationRequest(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.hash_user_public = builder.hash_user_public;
        this.uuid=builder.uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getHash_user_public() {
        return hash_user_public;
    }

    public String getUuid(){
        return uuid;
    }

    // Builder class
    public static class Builder {
        private String email;
        private String password;
        private String hash_user_public;
        private String uuid;
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder hash_user_public(String hash_user_public) {
            this.hash_user_public = hash_user_public;
            return this;
        }
        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }
        public AuthorizationRequest build() {
            return new AuthorizationRequest(this);
        }
    }
}

