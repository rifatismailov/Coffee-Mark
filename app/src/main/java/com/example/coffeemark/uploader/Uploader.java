package com.example.coffeemark.uploader;

import android.app.Activity;
import android.content.Context;


import com.example.coffeemark.uploader.progress.ProgressRequestBody;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Клас Uploader відповідає за завантаження файлів на сервер.
 */
public class Uploader {
    private final Operation operation;
    private final Context context;
    private final String server_address; // Адреса сервера

    /**
     * Конструктор класу Uploader.
     *
     * @param context        Контекст додатку.
     * @param server_address Адреса сервера, на який буде завантажено файл.
     */
    public Uploader(Context context, String server_address) {
        this.context = context;
        this.operation = (Operation) context;
        this.server_address = server_address;
    }

    /**
     * Метод для завантаження файлу на сервер.
     *
     * @param file Файл, який потрібно завантажити.
     */
    public void uploadFile(File file) {
        OkHttpClient client = new OkHttpClient();

        // Обгортка файлу для відстеження прогресу завантаження
        ProgressRequestBody fileBody = new ProgressRequestBody(file, "application/octet-stream", new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                setProgress(percentage, "");
            }

            @Override
            public void onError(String e) {
                setProgress(0, "Error: " + e);
            }

            @Override
            public void onFinish() {
                // Викликається після завершення завантаження
            }
        });

        // Формування запиту з файлом
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(server_address)
                .post(requestBody)
                .build();

        // Виконання HTTP-запиту
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setProgress(0, "Error: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    setProgress(0, "No Successfully");
                } else {
                    setProgress(100, "Successfully");
                }
            }
        });
    }

    private void setProgress(int progress, String info) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> operation.setProgress(progress, info));
        }
    }

    public interface Operation {
        /**
         * Метод для відображення прогресу
         *
         * @positionId ID позиція у активності
         * @progress прогрес дії
         * @info інформація яка можливо виникла під час прогресу
         */
        void setProgress(int progress, String info);

        /**
         * Метод який використовується під час завершення прогресу
         *
         * @positionId ID позиція у активності
         * @info інформація яка можливо виникла під час прогресу
         */
        void endProgress(String positionId, String info);
    }
}
