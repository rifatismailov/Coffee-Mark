package com.example.coffeemark.authorization;

import static com.example.coffeemark.util.KeyProvider.loadPublicKey;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.FieldValidator;
import com.example.coffeemark.registration.RegisterActivity;
import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.authorization.AuthorizationResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;
import com.example.coffeemark.user.User;
import com.example.coffeemark.util.Encryptor;

import java.security.PublicKey;

public class AuthorizationActivity extends AppCompatActivity {
    private EditText email, password;
    private Button login, registration;

    public void startRegistration() {
        // Запуск сервісу
        Intent serviceIntent = new Intent(this, RegisterActivity.class);
        startActivity(serviceIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        registration = findViewById(R.id.register_button);
        try {
            PublicKey publicKey = loadPublicKey(this);
            login.setOnClickListener(view -> {
                registerUser(publicKey);
            });
            registration.setOnClickListener(view -> startRegistration());
        } catch (Exception e) {
            Log.e("AuthorizationActivity", "Не вдалося завантажити публічний ключ", e);
            Toast.makeText(this, "Помилка: неможливо авторизуватися. Спробуйте пізніше.", Toast.LENGTH_LONG).show();

            // Деактивуємо кнопки
            login.setEnabled(false);
            registration.setEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser(PublicKey publicKey) {

        String userPassword = password.getText().toString();
        String userEmail = email.getText().toString();
        boolean isValid = FieldValidator.areFieldsValidAuthorization(userEmail, userPassword);

        if (isValid) {
            try {
                AuthorizationRequest request = new AuthorizationRequest.Builder()
                        .email(Encryptor.encryptText(userEmail, publicKey))
                        .password(Encryptor.encryptText(userPassword, publicKey))
                        .build();
                // Викликаємо метод з ApiHelper
                ApiHelper.authorization(request, new ApiHelper.ApiCallback<AuthorizationResponse>() {
                    @Override
                    public void onSuccess(AuthorizationResponse response) {

                        String message = response.getMessage();
                        if (response.isSuccess()) {
                            Log.e("AuthorizationActivity", "Message: " + message);

                        } else {
                            Toast.makeText(AuthorizationActivity.this, "Помилка: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String errorMessage, int code) {
                        Log.e("RegisterActivity", "Помилка " + code + ": " + errorMessage);
                        Toast.makeText(AuthorizationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Toast.makeText(AuthorizationActivity.this, "Помилка під час шифрування", Toast.LENGTH_SHORT).show();
                Log.e("AuthorizationActivity", "Помилка під час шифрування " + e);

            }

        } else {
            // Поля не заповнені
            Toast.makeText(AuthorizationActivity.this, "Заповніть буд ласка всі поля", Toast.LENGTH_SHORT).show();
        }

    }
}