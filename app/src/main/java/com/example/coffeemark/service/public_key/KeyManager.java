package com.example.coffeemark.service.public_key;

import static com.example.coffeemark.util.KeyProvider.loadPublicKey;
import static com.example.coffeemark.util.KeyProvider.savePublicKey;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.coffeemark.service.ApiHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class KeyManager {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void checkPublicKey(Context context) {
        try {
            PublicKey publicKey = loadPublicKey(context);
            String hash = getPublicKeyHash(publicKey);
            getPublicKey(context, hash);
        } catch (Exception e) {
            Log.e("KeyManager", "Public Key Exception " + e);
            getPublicKey(context, "");
        }

    }

    private static void getPublicKey(Context context, String hash) {
        ApiHelper.getPublicKey(new PublicKeyRequest(hash),new ApiHelper.ApiCallback<PublicKeyResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(PublicKeyResponse response) {
                // Отримуємо публічний ключ
                String response_Message = response.getMessage();
                //перевіряєм вміст тексту якщо воно не співпадає з hash то це Public Key
                if(!hash.equals(response_Message)) {
                    // Тепер можна завантажити цей ключ і використовувати для шифрування
                    try {
                        Log.e("KeyManager", "Public Key " + response_Message);
                        savePublicKey(response_Message, context);
                        //PublicKey publicKey = loadPublicKey(publicKeyPem);
//                    PublicKey publicKey = loadPublicKey(context);
//                    Log.e("KeyManager", "Public Key " + publicKey.hashCode());

                        //savePublicKey(publicKeyPem, context);
                        // Можеш далі шифрувати дані за допомогою цього ключа
                    } catch (Exception e) {
                        Log.e("KeyManager", "Get Public Key Exception " + e);
                    }
                }else {
                    Log.e("KeyManager", "Public Key hash " + response_Message);
                }
            }

            @Override
            public void onError(String message, int code) {
                Log.e("KeyManager", "Error from the server : " + message + " code " + code);

            }
        });
    }

    public static String getPublicKeyHash(PublicKey publicKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(publicKey.getEncoded());

        // Перетворення байтів у hex-рядок
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
