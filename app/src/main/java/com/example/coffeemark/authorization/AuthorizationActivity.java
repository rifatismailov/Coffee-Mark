package com.example.coffeemark.authorization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.RegisterActivity;
import com.example.coffeemark.user.DatabaseHelper;

public class AuthorizationActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login, registration;

    public void startRegistration() {
        // Запуск сервісу
        Intent serviceIntent = new Intent(this, RegisterActivity.class);
        startActivity(serviceIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        registration = findViewById(R.id.register_button);

        login.setOnClickListener(view -> {

        });
        registration.setOnClickListener(view -> startRegistration());

    }
}