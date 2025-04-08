package com.example.coffeemark.account;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class AccountManager {

    private static SharedPreferences getPrefs(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                "secure_prefs_coffee_mark",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    // Зберегти акаунт
    public static void saveAccount(Context context, Account account) {
        try {
            SharedPreferences prefs = getPrefs(context);
            prefs.edit()
                    .putString("username", account.getUsername())
                    .putString("password", account.getPassword())
                    .putString("email", account.getEmail())
                    .putString("role", account.getRole())
                    .putString("image", account.getImage())
                    .apply();
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
        }
    }

    // Отримати username
    public static String getUsername(Context context) {
        try {
            return getPrefs(context).getString("username", null);
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
            return null;
        }
    }
    // Отримати password
    public static String getPassword(Context context) {
        try {
            return getPrefs(context).getString("password", null);
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
            return null;
        }
    }
    // Отримати email
    public static String getEmail(Context context) {
        try {
            return getPrefs(context).getString("email", null);
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
            return null;
        }
    }

    // Отримати role
    public static String getRole(Context context) {
        try {
            return getPrefs(context).getString("role", null);
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
            return null;
        }
    }
    // Отримати image
    public static String getImage(Context context) {
        try {
            return getPrefs(context).getString("image", null);
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
            return null;
        }
    }
    // Очистити акаунт
    public static void clearAccount(Context context) {
        try {
            getPrefs(context).edit().clear().apply();
        } catch (Exception e) {
            Log.e("AccountManager",e.toString());
        }
    }
}

