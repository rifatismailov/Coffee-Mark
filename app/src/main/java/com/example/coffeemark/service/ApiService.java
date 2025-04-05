package com.example.coffeemark.service;

import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.authorization.AuthorizationResponse;
import com.example.coffeemark.service.public_key.PublicKeyRequest;
import com.example.coffeemark.service.public_key.PublicKeyResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("/api/auth/public-key")
    Call<PublicKeyResponse> getPublicKey(@Body PublicKeyRequest request); // новий метод


    @POST("/api/auth/authorization")
    Call<AuthorizationResponse> getAuthorization(@Body AuthorizationRequest request); // новий метод
}

