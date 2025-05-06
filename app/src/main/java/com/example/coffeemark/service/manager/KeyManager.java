package com.example.coffeemark.service.manager;

import static com.example.coffeemark.util.KeyUntil.getPublicKeyHash;
import static com.example.coffeemark.util.KeyUntil.loadPublicKey;
import static com.example.coffeemark.util.KeyUntil.saveKey;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.service.public_key.LocalPublicKeyRequest;
import com.example.coffeemark.service.public_key.LocalPublicKeyResponse;
import com.example.coffeemark.service.public_key.PublicKeyRequest;
import com.example.coffeemark.service.public_key.PublicKeyResponse;

import java.security.PublicKey;

public class KeyManager {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void checkPublicKey(Context context) {
        try {
            PublicKey publicKey = loadPublicKey(context, "public.pem");
            String hash = getPublicKeyHash(publicKey);
            getPublicKey(context, hash);
        } catch (Exception e) {
            Log.e("KeyManager", "Public Key Exception " + e);
            getPublicKey(context, "");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void getPublicKey(Context context, String hash) {
        ApiHelper.getPublicKey(new PublicKeyRequest(hash), new ApiHelper.ApiCallback<PublicKeyResponse>() {
            @Override
            public void onSuccess(PublicKeyResponse response) {
                // Отримуємо публічний ключ
                String response_Message = response.getMessage();
                //перевіряєм вміст тексту якщо воно не співпадає з hash то це Public Key
                if (!hash.equals(response_Message)) {
                    // Тепер можна завантажити цей ключ і використовувати для шифрування
                    try {
                        saveKey("public.pem", response_Message, context);
                    } catch (Exception e) {
                        Log.e("KeyManager", "Get Public Key Exception " + e);
                    }
                } else {
                    Log.e("KeyManager", "Public Key hash " + response_Message);
                }
            }

            @Override
            public void onError(String message, int code) {
                switch (code) {
                    case 400:
                        Log.e("AuthorizationActivity", "Невірний запит (Bad Request)");
                        break;
                    case 401:
                        Log.e("AuthorizationActivity", "Неавторизовано (Unauthorized)");
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

    public static void setLocalPublicKey(AuthManager.Authorization authorization, LocalPublicKeyRequest request) {
        ApiHelper.setLocalPublicKey(request, new ApiHelper.ApiCallback<LocalPublicKeyResponse>() {
            @Override
            public void onSuccess(LocalPublicKeyResponse response) {
                String message = response.getMessage();
                if (response.isSuccess()) {
                    authorization.onSuccess(message);

                } else {
                    authorization.onError(message);
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
                        authorization.onError(errorMessage);
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
                        authorization.onError(errorMessage);
                        break;
                    default:
                        Log.e("AuthorizationActivity", "Інша помилка: " + code);
                }
            }
        });
    }
}
