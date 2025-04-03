package com.example.coffeemark.registration;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.cafe.Cafe;
import com.example.coffeemark.registration.cafe.CafeAdapter;
import com.example.coffeemark.view.CustomButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, password, email, cafeName, cafeAddress;
    private Spinner roleSpinner;
    private LinearLayout cafeLayout;
    private Button addCafeButton;
    private CustomButton registerButton;
    private final List<Cafe> cafeList = new ArrayList<>();
    private CafeAdapter adapter;
    private RecyclerView recyclerView;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        roleSpinner = findViewById(R.id.roleSpinner);
        cafeLayout = findViewById(R.id.cafe_layout);
        cafeName = findViewById(R.id.cafeName);
        cafeAddress = findViewById(R.id.cafeAddress);
        addCafeButton = findViewById(R.id.add_cafe_button);
        registerButton = findViewById(R.id.register_button);
        recyclerView = findViewById(R.id.cafeList);

        // Ініціалізація списку та адаптера
        adapter = new CafeAdapter(cafeList);
        //cafeList.add(new Cafe("cafe name 1","cafe address 1"));
        //cafeList.add(new Cafe("cafe name 2","cafe address 2"));
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
                cafeList.add(new Cafe(name, address));
                adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView

                cafeName.setText("");
                cafeAddress.setText("");
                Toast.makeText(this, "Кав'ярня додана", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            registerButton.onPress("Please wait");
            registerUser();
        });
    }

    private void registerUser() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String mail = email.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        RegisterRequest request = new RegisterRequest(user, pass, mail, role, role.equals("BARISTA") ? cafeList:null);
        for (Cafe cafe:request.getCafes()){
            Log.e("RegisterActivity", "Cafe: " + cafe.getName());
        }
        Log.e("RegisterActivity", "CafeList: " + request.getCafes().size()+" "+role);

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
    }
}
