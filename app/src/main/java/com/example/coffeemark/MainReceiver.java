package com.example.coffeemark;

import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MainReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals("com.example.COFFEE_MARK")) {
            String message = intent.getStringExtra("account");
            // Оновлення інтерфейсу з новим повідомленням
            Log.e("MainActivity", message);
            if ("Authorization".equals(message)) {
                Log.e("MainActivity", message);
            }
            if ("Registration".equals(message)) {
                Log.e("MainActivity", message);
            }
        }
    }

}
