package com.example.coffeemark.service;

import com.example.coffeemark.service.public_key.PublicKeyRequest;
import com.example.coffeemark.service.public_key.PublicKeyResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHelper {

    public interface ApiCallback<T> {
        void onSuccess(T response);
        void onError(String errorMessage, int code);
    }

    public static <T> void sendRequest(Call<T> call, ApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Невідома помилка";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string(); // деталізоване повідомлення
                        }
                    } catch (IOException e) {
                        errorMsg = e.getMessage();
                    }
                    callback.onError(errorMsg, response.code());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onError(t.getMessage(), -1);
            }
        });
    }
    public static void register(RegisterRequest request, ApiCallback<RegisterResponse> callback) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        sendRequest(api.registerUser(request), callback);
    }

    public static void getPublicKey(ApiCallback<PublicKeyResponse> callback) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        sendRequest(api.getPublicKey(new PublicKeyRequest("PublicKey")), callback);
    }
}

