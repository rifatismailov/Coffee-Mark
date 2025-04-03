package com.example.coffeemark.registration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/api/auth/register") // Адреса реєстрації на сервері (замініть за потреби)
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);
}
