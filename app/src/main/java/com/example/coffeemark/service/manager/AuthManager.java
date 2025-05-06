package com.example.coffeemark.service.manager;

import android.util.Log;

import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.authorization.AuthorizationResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;

/**
 * Менеджер аутентифікації та реєстрації користувачів.
 * Відповідає за ініціацію запитів до API (через ApiHelper),
 * а також за обробку успішних відповідей та помилок.
 */
public class AuthManager {

    /**
     * Реєстрація користувача через сервер API.
     *
     * @param registration об'єкт колбеку, що обробляє результат реєстрації.
     * @param request      запит, що містить дані користувача для реєстрації.
     */
    public static void registration(Registration registration, RegisterRequest request) {
        // Викликаємо метод із ApiHelper
        ApiHelper.register(request, new ApiHelper.ApiCallback<RegisterResponse>() {
            @Override
            public void onSuccess(RegisterResponse response) {
                String message = response.getMessage();
                Log.e("RegisterActivity", "Message: " + message);

                if (response.isSuccess()) {
                    registration.onSuccess(message);
                } else {
                    registration.onError(message);
                }
            }

            @Override
            public void onError(String errorMessage, int code) {
                Log.e("RegisterActivity", "HTTP-код помилки: " + code);
                Log.e("RegisterActivity", "Повідомлення: " + errorMessage);

                // Логування типових помилок HTTP
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

                // Передаємо повідомлення про помилку назад у колбек
                registration.onError(errorMessage);
            }
        });
    }

    /**
     * Авторизація користувача через сервер API.
     *
     * @param message_authorization об'єкт колбеку, що обробляє результат авторизації.
     * @param request               запит із даними для авторизації (логін, пароль тощо).
     */
    public static void authorization(Authorization message_authorization, AuthorizationRequest request) {
        // Викликаємо метод із ApiHelper
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
                // Логування типових помилок HTTP
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

    /**
     * Інтерфейс для обробки результатів авторизації користувача.
     */
    public interface Authorization {
        /**
         * Викликається при успішній авторизації.
         *
         * @param message повідомлення з сервера (наприклад, токен або статус).
         */
        void onSuccess(String message);

        /**
         * Викликається при помилці авторизації або відмові.
         *
         * @param message повідомлення про помилку.
         */
        void onError(String message);

        /**
         * Повертає локальний публічний ключ клієнта, якщо він є.
         *
         * @return локальний публічний ключ у форматі рядка.
         */
        String getLocalPublicKey();
    }

    /**
     * Інтерфейс для обробки результатів реєстрації користувача.
     */
    public interface Registration {
        /**
         * Викликається при успішній реєстрації.
         *
         * @param message повідомлення з сервера.
         */
        void onSuccess(String message);

        /**
         * Викликається при помилці реєстрації або відмові.
         *
         * @param message повідомлення про помилку.
         */
        void onError(String message);

        /**
         * Метод для обробки додаткового повідомлення з сервера (опційно).
         *
         * @param message довільне повідомлення (не завжди використовується).
         */
        void onMessage(String message);
    }
}
