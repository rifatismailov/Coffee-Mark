package com.example.coffeemark.service;

import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.authorization.AuthorizationResponse;
import com.example.coffeemark.service.public_key.LocalPublicKeyRequest;
import com.example.coffeemark.service.public_key.LocalPublicKeyResponse;
import com.example.coffeemark.service.public_key.PublicKeyRequest;
import com.example.coffeemark.service.public_key.PublicKeyResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    @POST("/api/auth/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);

    @POST("/api/auth/authorization")
    Call<AuthorizationResponse> getAuthorization(@Body AuthorizationRequest request);

    @POST("/api/auth/public-key")
    Call<PublicKeyResponse> getPublicKey(@Body PublicKeyRequest request);

    @POST("/api/auth/local-public-key")
    Call<LocalPublicKeyResponse> setLocalPublicKey(@Body LocalPublicKeyRequest request);

    @Multipart
    @POST("/api/files/upload")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);


    @GET("/api/files/download/{fileName}")
    Call<ResponseBody> downloadFile(@Path("fileName") String fileName);

}

