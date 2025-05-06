package com.example.coffeemark.service.manager;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.progress.ProgressRequestBody;

import java.io.File;

import okhttp3.ResponseBody;

public class FileTransferManager {

    public static void uploadFile(File file, FileTransferCallback uploader) {
        ApiHelper.uploadFile(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {

                new Handler(Looper.getMainLooper()).post(() -> {
                    uploader.onProgress(percentage);
                });
            }

            @Override
            public void onError(String e) {

            }
        }, new ApiHelper.ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody response) {
                uploader.onFinish();
            }

            @Override
            public void onError(String errorMessage, int code) {
                uploader.onFileError(errorMessage);
            }
        });
    }


    /**
     * Завантажує файл з вказаного URL у зазначену папку.
     *
     * @param fileUrl         URL файлу.
     * @param destinationFile Файл, куди потрібно зберегти завантаження.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void downloadFile(FileTransferCallback downloader,String fileUrl, File destinationFile) {
        ApiHelper.downloadFile(fileUrl, destinationFile, new ApiHelper.ApiCallbackFile<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody response) {
                new Handler(Looper.getMainLooper()).post(downloader::onFinish);
            }

            @Override
            public void onError(String errorMessage, int code) {
                new Handler(Looper.getMainLooper()).post(() -> downloader.onFileError(errorMessage));
            }

            @Override
            public void onProgressUpdate(int percentage) {
                new Handler(Looper.getMainLooper()).post(() -> downloader.onProgress(percentage));
            }

        });
    }

    /**
     * Універсальний інтерфейс для обробки подій передачі файлів (завантаження/відправка).
     */
    public interface FileTransferCallback {
        /**
         * Метод для відображення прогресу
         *
         * @param progress прогрес у відсотках
         */
        void onProgress(int progress);

        /**
         * Метод при виникненні помилки
         *
         * @param e текст помилки
         */
        void onFileError(String e);

        /**
         * Метод який викликається при завершенні дії
         */
        void onFinish();
    }

}
