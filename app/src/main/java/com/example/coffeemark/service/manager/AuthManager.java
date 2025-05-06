package com.example.coffeemark.service.manager;

import android.util.Log;

import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.authorization.AuthorizationResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;

public class AuthManager {
    public static void registration(ManagerRegistration managerRegistration, RegisterRequest request) {
        // Викликаємо метод з ApiHelper
        ApiHelper.register(request, new ApiHelper.ApiCallback<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse response) {
                String message = response.getMessage();
                Log.e("RegisterActivity", "Message: " + message);

                if (response.isSuccess()) {
                    managerRegistration.onSuccess(message);

                } else {
                    managerRegistration.onError(message);
                }
            }

            @Override
            public void onError(String errorMessage, int code) {
                Log.e("RegisterActivity", "HTTP-код помилки: " + code);
                Log.e("RegisterActivity", "Повідомлення: " + errorMessage);

                switch (code) {
                    case 400:
                        Log.e("RegisterActivity", "Невірний запит (Bad Request)");
                        break;
                    case 401:
                        Log.e("RegisterActivity", "Неавторизовано (Unauthorized)");
                        break;
                    case 403:
                        Log.e("RegisterActivity", "Заборонено (Forbidden)");
                        break;
                    case 404:
                        Log.e("RegisterActivity", "Ресурс не знайдено (Not Found)");
                        break;
                    case 409:
                        Log.e("RegisterActivity", "Користувач вже існує (Conflict)");
                        break;
                    case 500:
                        Log.e("RegisterActivity", "Внутрішня помилка сервера (Internal Server Error)");
                        break;
                    default:
                        Log.e("RegisterActivity", "Інша помилка: " + code);
                }

                managerRegistration.onError(errorMessage);
            }
        });
    }

    public static void authorization(MessageAuthorization message_authorization, AuthorizationRequest request) {
        // Викликаємо метод з ApiHelper
        ApiHelper.authorization(request, new ApiHelper.ApiCallback<AuthorizationResponse>() {
            @Override
            public void onSuccess(AuthorizationResponse response) {

                String message = response.getMessage();
                if (response.isSuccess()) {
                    message_authorization.onSuccess(message);

                } else {
                    message_authorization.onError(message);
                }
            }

            @Override
            public void onError(String errorMessage, int code) {
                switch (code) {
                    case 400:
                        Log.e("AuthorizationActivity", "Невірний запит (Bad Request)");
                        break;
                    case 401:
                        Log.e("AuthorizationActivity", "Неавторизовано (Unauthorized)");
                        message_authorization.onError(errorMessage);
                        break;
                    case 403:
                        Log.e("AuthorizationActivity", "Заборонено (Forbidden)");
                        break;
                    case 404:
                        Log.e("AuthorizationActivity", "Ресурс не знайдено (Not Found)");
                        break;
                    case 409:
                        Log.e("AuthorizationActivity", "Користувач вже існує (Conflict)");
                        break;
                    case 500:
                        Log.e("AuthorizationActivity", "Внутрішня помилка сервера (Internal Server Error)");
                        break;
                    default:
                        Log.e("AuthorizationActivity", "Інша помилка: " + code);
                }
            }
        });
    }

    public interface MessageAuthorization {
        void onSuccess(String message);

        void onError(String message);

        String getLocalPublicKey();
    }

    public interface ManagerRegistration {
        void onSuccess(String message);

        void onError(String message);

        void onMessage(String message);
    }
}
