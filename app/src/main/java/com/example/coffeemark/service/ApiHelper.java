package com.example.coffeemark.service;

import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.authorization.AuthorizationResponse;
import com.example.coffeemark.service.public_key.LocalPublicKeyRequest;
import com.example.coffeemark.service.public_key.LocalPublicKeyResponse;
import com.example.coffeemark.service.public_key.PublicKeyRequest;
import com.example.coffeemark.service.public_key.PublicKeyResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;
import com.example.coffeemark.progress.ProgressRequestBody;
import com.example.coffeemark.progress.ProgressResponseBody;
import com.example.coffeemark.service.search.SearchRequest;
import com.example.coffeemark.service.search.SearchResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Клас-допоміжник для роботи з API через Retrofit.
 * Містить універсальні методи для надсилання запитів, обробки відповіді,
 * завантаження/відправки файлів та обробки прогресу.
 */
public class ApiHelper {

    /**
     * Універсальний інтерфейс для обробки результатів API-запитів без прогресу.
     *
     * @param <T> Тип об'єкта, що повертається у відповіді (наприклад, AuthorizationResponse).
     */
    public interface ApiCallback<T> {

        /**
         * Метод викликається, коли запит виконується успішно.
         *
         * @param response відповідь з сервера у вигляді об'єкта типу T
         */
        void onSuccess(T response);

        /**
         * Метод викликається при помилці запиту.
         *
         * @param errorMessage повідомлення про помилку
         * @param code         код відповіді HTTP або -1 при збої з'єднання
         */
        void onError(String errorMessage, int code);
    }

    /**
     * Інтерфейс для обробки результатів API-запитів з підтримкою прогресу.
     *
     * @param <T> Тип об'єкта, що повертається у відповіді (наприклад, ResponseBody).
     */
    public interface ApiCallbackFile<T> {

        /**
         * Метод викликається, коли запит виконується успішно.
         *
         * @param response відповідь з сервера
         */
        void onSuccess(T response);

        /**
         * Метод викликається при помилці запиту.
         *
         * @param errorMessage повідомлення про помилку
         * @param code         код відповіді HTTP або -1 при збої з'єднання
         */
        void onError(String errorMessage, int code);

        /**
         * Метод викликається для оновлення прогресу завантаження або відправки.
         *
         * @param percentage відсоток виконання дії (0–100)
         */
        void onProgressUpdate(int percentage);
    }

    /**
     * Універсальний метод для відправки HTTP-запиту через Retrofit і обробки відповіді.
     *
     * @param call     екземпляр Retrofit Call, який виконує запит
     * @param callback обробник результату запиту
     * @param <T>      тип відповіді, яку повертає API
     */
    public static <T> void sendRequest(Call<T> call, ApiCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Успішна відповідь з тілом
                    callback.onSuccess(response.body());
                } else {
                    // Відповідь з помилкою або без тіла
                    String errorMsg = "Невідома помилка";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        errorMsg = e.getMessage();
                    }
                    callback.onError(errorMsg, response.code());
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                // Сталася помилка під час виконання запиту (наприклад, відсутній інтернет)
                callback.onError(t.getMessage(), -1);
            }
        });
    }

    /**
     * Авторизація користувача через API.
     *
     * @param request  об'єкт з даними авторизації
     * @param callback обробник відповіді
     */
    public static void authorization(AuthorizationRequest request, ApiCallback<AuthorizationResponse> callback) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        sendRequest(api.authorization(request), callback);
    }

    /**
     * Реєстрація нового користувача.
     *
     * @param request  об'єкт з даними для реєстрації
     * @param callback обробник відповіді
     */
    public static void register(RegisterRequest request, ApiCallback<RegisterResponse> callback) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        sendRequest(api.registration(request), callback);
    }

    /**
     * Пошук.
     *
     * @param request  об'єкт з даними для пошуку
     * @param callback обробник відповіді
     */
    public static void search(SearchRequest request, ApiCallback<SearchResponse> callback) {
        ApiService api = RetrofitClient.getClientReg().create(ApiService.class);
        sendRequest(api.search(request), callback);
    }

    /**
     * Отримання відкритого ключа з сервера.
     *
     * @param request  об'єкт із параметрами запиту
     * @param callback обробник відповіді
     */
    public static void getPublicKey(PublicKeyRequest request, ApiCallback<PublicKeyResponse> callback) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        sendRequest(api.getPublicKey(request), callback);
    }

    /**
     * Відправлення локального публічного ключа на сервер.
     *
     * @param request  об'єкт з публічним ключем
     * @param callback обробник відповіді
     */
    public static void setLocalPublicKey(LocalPublicKeyRequest request, ApiCallback<LocalPublicKeyResponse> callback) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        sendRequest(api.setLocalPublicKey(request), callback);
    }

    /**
     * Відправлення файлу на сервер з можливістю відстеження прогресу.
     *
     * @param file     файл, який потрібно надіслати
     * @param listener слухач для оновлення прогресу
     * @param callback обробник відповіді
     */
    public static void uploadFile(File file, ProgressRequestBody.UploadCallbacks listener, ApiCallback<ResponseBody> callback) {
        // Обгортка файлу для підтримки оновлення прогресу
        ProgressRequestBody fileBody = new ProgressRequestBody(file, "application/octet-stream", listener);

        // Формуємо multipart-запит
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        // Виконуємо запит через Retrofit
        ApiService api = RetrofitClient.getClientFS().create(ApiService.class);
        Call<ResponseBody> call = api.uploadFile(filePart);

        sendRequest(call, callback);
    }

    /**
     * Завантажує файл з вказаного імені у зазначену папку.
     *
     * @param fileName        URL файлу.
     * @param destinationFile Файл, куди потрібно зберегти завантаження.
     */
    public static void downloadFile(String fileName, File destinationFile, ApiCallbackFile<ResponseBody> callback) {

        ApiService api = RetrofitClient.getClientFS().create(ApiService.class);
        /**
         * ResponseBody — це клас з Retrofit/OkHttp, який представляє тіло HTTP-відповіді (наприклад, PDF-файл, зображення, будь-що у байтах).
         * Це — ініціалізація запиту до API.
         * Call<ResponseBody> — об'єкт, який представляє HTTP-запит
         * api.downloadFile(fileName) — метод, який звертається до твого бекенду (@GET, @POST і т.д.)
         */
        Call<ResponseBody> call = api.downloadFile(fileName);
        call.enqueue(new Callback<ResponseBody>() {//Я хочу відправити запит у фоновому потоці (щоб UI не зависав) і обробити відповідь коли вона прийде.
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) { // onResponse(...) — якщо все добре
                /**
                 * response.body() — це ResponseBody, тобто сам файл
                 * response.isSuccessful() — перевірка, чи код 2xx
                 */
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Обгортка ResponseBody у ProgressResponseBody для відстеження прогресу
                        //Усередині ми:

                        //Обгортаємо відповідь у ProgressResponseBody, щоб відслідковувати прогрес
                        //Зчитуємо байти з сервера → записуємо їх у файл
                        ProgressResponseBody progressResponseBody = new ProgressResponseBody(response.body(), new ProgressResponseBody.DownloadCallbacks() {
                            @Override
                            public void onProgressUpdate(int percentage) {
                                // Оновлення прогресу завантаження
                                callback.onProgressUpdate(percentage);
                            }

                            @Override
                            public void onError(String e) {
                                callback.onError(e, -1); // Обробка помилок
                            }
                        });

                        // Записуємо файл у зазначену директорію
                        try (InputStream inputStream = progressResponseBody.byteStream();
                             FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
                            byte[] buffer = new byte[2048];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, bytesRead);
                            }
                            callback.onSuccess(progressResponseBody);
                        }
                    } catch (IOException e) {
                        callback.onError("Download failed: " + e.getMessage(), response.code());
                    }
                } else {
                    callback.onError("Failed to download file", response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError("Download error: " + t.getMessage(), -1);
            }
        });
    }


}

