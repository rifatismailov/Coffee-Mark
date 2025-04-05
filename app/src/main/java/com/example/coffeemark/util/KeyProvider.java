package com.example.coffeemark.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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
}
