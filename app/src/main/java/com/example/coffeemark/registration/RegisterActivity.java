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

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.cafe.Cafe;
import com.example.coffeemark.registration.cafe.CafeAdapter;
import com.example.coffeemark.service.ApiHelper;
import com.example.coffeemark.service.ApiService;
import com.example.coffeemark.service.RetrofitClient;
import com.example.coffeemark.service.public_key.PublicKeyResponse;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.service.registration.RegisterResponse;
import com.example.coffeemark.uploader.Uploader;
import com.example.coffeemark.util.ImageHandler;
import com.example.coffeemark.util.UrlBuilder;
import com.example.coffeemark.user.DatabaseHelper;
import com.example.coffeemark.user.User;
import com.example.coffeemark.view.CoffeeView;
import com.example.coffeemark.view.CustomButton;

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
        EdgeToEdge.enable(this);//— це спосіб зробити ваш UI більш адаптивним, гнучким та "повноекранним".
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
                cafeList.add(new Cafe(name, address, image != null ? image : ""));
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

            RegisterRequest request = new RegisterRequest.Builder()
                    .username(userName)
                    .password(userPassword)
                    .email(userEmail)
                    .role(role)
                    .cafes(role.equals("BARISTA") ? cafeList : null)
                    .image(image != null ? image : "coffee_mark.png")
                    .build();

            // Викликаємо метод з ApiHelper
            ApiHelper.register(request, new ApiHelper.ApiCallback<RegisterResponse>() {
                @Override
                public void onSuccess(RegisterResponse response) {
                    String message = response.getMessage();
                    Log.e("RegisterActivity", "Message: " + message);

                    if (response.isSuccess()) {
                        registerButton.stopLoading();
                        registerButton.setMessage(message);
                        User user = new User(userName, userPassword, userEmail, role, image != null ? image : "");
                        dbHelper.setUser(user);
                        addMessageToActivity(user.toJson().toString());
                        dbHelper.getAllUsers(); // Додатково, якщо потрібно
                    } else {
                        Toast.makeText(RegisterActivity.this, "Помилка: " + message, Toast.LENGTH_SHORT).show();
                        registerButton.stopLoading();
                    }
                }

                @Override
                public void onError(String errorMessage, int code) {
                    Log.e("RegisterActivity", "Помилка " + code + ": " + errorMessage);
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    registerButton.stopLoading();
                }
            });

        } else {
            // Поля не заповнені
            Toast.makeText(RegisterActivity.this, "Заповніть буд ласка всі поля", Toast.LENGTH_SHORT).show();
        }

    }

    private static final String ACTION_REGISTRATION_MESSAGE = "com.example.COFFEE_MARK";
    private static final String EXTRA_MESSAGE = "account";

    private void addMessageToActivity(String message) {
        Intent intent = new Intent(ACTION_REGISTRATION_MESSAGE);
        intent.putExtra(EXTRA_MESSAGE, message);
        sendBroadcast(intent);  // Надсилання повідомлення Activity 1
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
            if (!cafeList.isEmpty()) {
                for (Cafe cafe : cafeList) {
                    cafe.setCafe_image(image);
                }
                adapter.notifyDataSetChanged(); // Оновлюємо весь список
            }
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
