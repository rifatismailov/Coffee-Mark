package com.example.coffeemark.service.manager;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.progress.ProgressRequestBody;

import java.io.File;

import okhttp3.ResponseBody;

/**
 * Менеджер для передачі файлів: завантаження на сервер та отримання з сервера.
 * Використовує колбеки для повідомлення про стан передачі файлів (прогрес, помилка, завершення).
 */
public class FileTransferManager {

    /**
     * Завантажує файл на сервер.
     *
     * @param file     Файл, який потрібно завантажити.
     * @param uploader Інтерфейс зворотного виклику для повідомлення про статус.
     */
    public static void uploadFile(File file, FileTransferCallback uploader) {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        ApiHelper.uploadFile(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
                mainHandler.post(() -> uploader.onProgress(percentage));
            }

            @Override
            public void onError(String e) {
                mainHandler.post(() -> uploader.onFileError(e));
            }
        }, new ApiHelper.ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody response) {
                mainHandler.post(uploader::onFinish);
            }

            @Override
            public void onError(String errorMessage, int code) {
                mainHandler.post(() -> uploader.onFileError(errorMessage));
            }
        });
    }

    /**
     * Завантажує файл з вказаного URL у зазначену папку.
     *
     * @param downloader      Інтерфейс зворотного виклику для повідомлення про статус.
     * @param fileUrl         URL файлу для завантаження.
     * @param destinationFile Місце, куди буде збережено завантажений файл.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadFile(FileTransferCallback downloader, String fileUrl, File destinationFile) {
        Handler mainHandler = new Handler(Looper.getMainLooper());

        ApiHelper.downloadFile(fileUrl, destinationFile, new ApiHelper.ApiCallbackFile<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody response) {
                mainHandler.post(downloader::onFinish);
            }

            @Override
            public void onError(String errorMessage, int code) {
                mainHandler.post(() -> downloader.onFileError(errorMessage));
            }

            @Override
            public void onProgressUpdate(int percentage) {
                mainHandler.post(() -> downloader.onProgress(percentage));
            }
        });
    }

    /**
     * Інтерфейс зворотного виклику для обробки подій, пов'язаних із передачею файлів (завантаження/відправка).
     */
    public interface FileTransferCallback {
        /**
         * Викликається для оновлення прогресу передачі.
         *
         * @param progress Поточний прогрес у відсотках.
         */
        void onProgress(int progress);

        /**
         * Викликається у випадку помилки під час передачі.
         *
         * @param e Опис помилки.
         */
        void onFileError(String e);

        /**
         * Викликається після успішного завершення передачі.
         */
        void onFinish();
    }
}
