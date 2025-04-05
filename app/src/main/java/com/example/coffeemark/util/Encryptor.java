package com.example.coffeemark.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

public class Encryptor {
    // Шифрування пароля
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptText(String password, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Шифруємо пароль і конвертуємо в Base64, щоб повернути у вигляді рядка
        byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
