package com.example.coffeemark.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class KeyProvider {
    // Функція для завантаження публічного ключа
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PublicKey loadPublicKey(String pem) throws Exception {
        pem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(pem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PublicKey loadPublicKey(Context context) throws Exception {
        // Читаємо публічний ключ з файлу
        FileInputStream fis = context.openFileInput("public.pem");
        StringBuilder keyBuilder = new StringBuilder();
        int character;

        // Читаємо з файлу та додаємо в StringBuilder
        while ((character = fis.read()) != -1) {
            keyBuilder.append((char) character);
        }
        fis.close();

        // Отримуємо PEM-форматований ключ як рядок
        String pem = keyBuilder.toString();

        return loadPublicKey(pem);
    }


    public static void savePublicKey(String pem, Context context) throws IOException {
        // Отримуємо потік для запису в файл у внутрішньому сховищі
        FileOutputStream fos = context.openFileOutput("public.pem", Context.MODE_PRIVATE);
        // Записуємо ключ у файл
        fos.write(pem.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

}
