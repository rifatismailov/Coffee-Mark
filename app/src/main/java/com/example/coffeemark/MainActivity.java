package com.example.coffeemark;

import static com.example.coffeemark.service.public_key.KeyManager.checkPublicKey;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coffeemark.authorization.AuthorizationActivity;


import com.example.coffeemark.user.DatabaseHelper;


public class MainActivity extends AppCompatActivity {

    private final BroadcastReceiver registrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals("com.example.COFFEE_MARK")) {
                String message = intent.getStringExtra("account");
                // Оновлення інтерфейсу з новим повідомленням
                Log.e("MainActivity", message);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerRegistrationActivity() {
        IntentFilter filter = new IntentFilter("com.example.COFFEE_MARK");
        registerReceiver(registrationBroadcastReceiver, filter, Context.RECEIVER_EXPORTED);
    }

    public void startRegistration() {
        // Запуск сервісу
        Intent serviceIntent = new Intent(this, AuthorizationActivity.class);
        startActivity(serviceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        checkPublicKey(this);
        new DatabaseHelper(this).deleteAllUsers();
        registerRegistrationActivity();
        startRegistration();
    }




}