package com.example.coffeemark.registration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.cafe.Cafe;
import com.example.coffeemark.registration.cafe.CafeAdapter;
import com.example.coffeemark.uploader.Uploader;
import com.example.coffeemark.util.ImageHandler;
import com.example.coffeemark.util.UrlBuilder;
import com.example.coffeemark.user.DatabaseHelper;
import com.example.coffeemark.user.User;
import com.example.coffeemark.view.CoffeeView;
import com.example.coffeemark.view.CustomButton;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements Uploader.Operation {
    private CoffeeView coffeeView;
    private EditText username, password, email, cafeName, cafeAddress;
    private Spinner roleSpinner;
    private LinearLayout cafeLayout;
    private Button addCafeButton;
    private CustomButton registerButton;
    private final List<Cafe> cafeList = new ArrayList<>();
    private CafeAdapter adapter;
    private ApiService apiService;
    private DatabaseHelper dbHelper;
    private String image;
    private ImageHandler imageHandler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DatabaseHelper(this);
        coffeeView = findViewById(R.id.coffee_view);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        roleSpinner = findViewById(R.id.roleSpinner);
        cafeLayout = findViewById(R.id.cafe_layout);
        cafeName = findViewById(R.id.cafeName);
        cafeAddress = findViewById(R.id.cafeAddress);
        addCafeButton = findViewById(R.id.add_cafe_button);
        registerButton = findViewById(R.id.register_button);
        RecyclerView recyclerView = findViewById(R.id.cafeList);

        imageHandler = new ImageHandler(this);
        // Ініціалізація списку та адаптера
        adapter = new CafeAdapter(cafeList, imageHandler);
        // Налаштовуємо RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        apiService = RetrofitClient.getClient().create(ApiService.class);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = parent.getItemAtPosition(position).toString();
                if (selectedRole.equals("BARISTA")) {
                    cafeLayout.setVisibility(View.VISIBLE);
                    addCafeButton.setVisibility(View.VISIBLE);
                } else {
                    cafeLayout.setVisibility(View.GONE);
                    addCafeButton.setVisibility(View.GONE);
                    cafeList.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addCafeButton.setOnClickListener(v -> {
            String name = cafeName.getText().toString();
            String address = cafeAddress.getText().toString();
            if (!name.isEmpty() && !address.isEmpty()) {
                cafeList.add(new Cafe(name, address, image));
                adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView

                cafeName.setText("");
                cafeAddress.setText("");
                Toast.makeText(this, "Кав'ярня додана", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            registerUser();
        });
        coffeeView.setOnClickListener(view -> openGallery());
    }

    private void registerUser() {
        String userName = username.getText().toString();
        String userPassword = password.getText().toString();
        String userEmail = email.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();
        boolean isValid = FieldValidator.areFieldsValid(userName, userPassword, userEmail, role);

        if (isValid) {
            registerButton.onPress("Please wait");
            RegisterRequest request = new RegisterRequest(userName, userPassword, userEmail, role, role.equals("BARISTA") ? cafeList : null,
                    !image.isEmpty() ? image : "");

            apiService.registerUser(request).enqueue(new Callback<RegisterResponse>() {

                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            RegisterResponse registerResponse = response.body();
                            String message = registerResponse.getMessage();

                            Log.e("RegisterActivity", "Message: " + message);

                            if (registerResponse.isSuccess()) {
                                Toast.makeText(RegisterActivity.this, "Message: " + message, Toast.LENGTH_SHORT).show();
                                registerButton.stopLoading();
                                dbHelper.setUser(new User(userName, userPassword, userEmail, role, image));

                                // Зчитуємо всіх користувачів
                                dbHelper.getAllUsers();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                                registerButton.stopLoading();
                            }
                        } else {
                            // Якщо тіло відповіді пусте
                            Log.e("RegisterActivity", "Response body is null");
                            Toast.makeText(RegisterActivity.this, "Response body is null", Toast.LENGTH_SHORT).show();

                            registerButton.stopLoading();

                        }
                    } else {
                        // Якщо статус код не успішний, вивести більше інформації
                        int statusCode = response.code();
                        String errorMessage = "Error: " + statusCode + " - " + response.message();
                        Log.e("RegisterActivity", errorMessage);
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        registerButton.stopLoading();

                    }
                }


                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    // Тут обробляємо випадки, коли виникає помилка з мережею або запитом
                    Log.e("RegisterActivity", t.getMessage());

                    Toast.makeText(RegisterActivity.this, "Не вдалося підключитися: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    registerButton.stopLoading();

                }
            });
        } else {
            // Поля не заповнені
            Toast.makeText(RegisterActivity.this, "Заповніть буд ласка всі поля", Toast.LENGTH_SHORT).show();
        }

    }

    // Вибір зображення
    private void openGallery() {
        // Потім просто викликаєш:
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }


    ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    handleImageUri(imageUri);
                    // Тут передаєш URI у свою функцію для обробки
                }
            }
    );

    // Метод для обробки зображення
    private void handleImageUri(Uri uri) {
        try {
            String serverUrl = new UrlBuilder.Builder()
                    .setProtocol("http")
                    .setIp("192.168.177.4")
                    .setPort("8020")
                    .setDirectory("/api/files/upload")
                    .build()
                    .buildUrl();


            coffeeView.setImageBitmap(imageHandler.getBitmap(uri));

            File savedFile = imageHandler.processAndSaveImage(uri);
            image = imageHandler.getSavedFileName();
            Log.e("RegisterActivity", "Saved to: " + savedFile.getAbsolutePath());

            new Uploader(this, serverUrl).uploadFile(savedFile);
        } catch (IOException e) {
            Log.e("RegisterActivity", "Image processing failed", e);
        }
    }


    @Override
    public void setProgress(int progress, String info) {
        Log.e("RegisterActivity", progress + " " + info);
    }

    @Override
    public void endProgress(String positionId, String info) {
        Log.e("RegisterActivity", positionId + " " + info);
    }
}
