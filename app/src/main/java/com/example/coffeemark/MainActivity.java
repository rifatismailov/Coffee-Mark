package com.example.coffeemark;

import static com.example.coffeemark.service.Manager.checkPublicKey;
import static com.example.coffeemark.util.KeyUntil.checkLocalKey;
import static com.example.coffeemark.util.KeyUntil.getPublicKeyHash;
import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.authorization.AuthorizationActivity;


import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.fragment.FragmentOne;
import com.example.coffeemark.cafe.CafeBase;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.fragment.FragmentTwo;
import com.example.coffeemark.service.Manager;
import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.search.SearchRequest;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.util.Encryptor;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CustomButton;
import com.example.coffeemark.view.CustomNumberPicker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Manager.ManagerSearch {

    private final BroadcastReceiver registrationAuthorizationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals("com.example.COFFEE_MARK")) {
                String message = intent.getStringExtra("account");
                // Оновлення інтерфейсу з новим повідомленням
                Log.e("MainActivity", message);
                if ("Authorization".equals(message)) {
                    try {
                        PrivateKey privateKey = loadPrivateKey(getBaseContext(), "user_private.pem");

                        String username = Decryptor.decryptText(AccountManager.getUsername(getBaseContext()), privateKey);
                        String password = Decryptor.decryptText(AccountManager.getPassword(getBaseContext()), privateKey);
                        String email = Decryptor.decryptText(AccountManager.getEmail(getBaseContext()), privateKey);

                        Log.e("MainActivity",
                                username + " "
                                        + password + " "
                                        + email + " "
                                        + AccountManager.getRole(getBaseContext()) + " "
                                        + AccountManager.getImage(getBaseContext()));
                    } catch (Exception e) {
                        Log.e("MainActivity", e.toString());
                    }

                }
                if ("Registration".equals(message)) {
                    try {
                        PrivateKey privateKey = loadPrivateKey(getBaseContext(), "user_private.pem");

                        String username = Decryptor.decryptText(AccountManager.getUsername(getBaseContext()), privateKey);
                        String password = Decryptor.decryptText(AccountManager.getPassword(getBaseContext()), privateKey);
                        String email = Decryptor.decryptText(AccountManager.getEmail(getBaseContext()), privateKey);

                        Log.e("MainActivity",
                                username + " "
                                        + password + " "
                                        + email + " "
                                        + AccountManager.getRole(getBaseContext()) + " "
                                        + AccountManager.getImage(getBaseContext()));
                    } catch (Exception e) {
                        Log.e("MainActivity", e.toString());
                    }

                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerRegistrationActivity() {
        IntentFilter filter = new IntentFilter("com.example.COFFEE_MARK");
        registerReceiver(registrationAuthorizationBroadcastReceiver, filter, Context.RECEIVER_EXPORTED);
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
        checkLocalKey(this);
        checkPublicKey(this);
        CustomButton customButton = findViewById(R.id.request_button);
      // Дані для вибору
        String[] searchOptions = {"Name", "Address","Barista","Coffee"};

        // new DatabaseHelper(this).deleteAllUsers();
        //registerRegistrationActivity();
        startRegistration();
        replaceFragment(new FragmentOne());

        try {
            PrivateKey privateKey = loadPrivateKey(getBaseContext(), "user_private.pem");
            String username = Decryptor.decryptText(AccountManager.getUsername(getBaseContext()), privateKey);
            String password = Decryptor.decryptText(AccountManager.getPassword(getBaseContext()), privateKey);
            String email = Decryptor.decryptText(AccountManager.getEmail(getBaseContext()), privateKey);

            Log.e("MainActivity",
                    username + " "
                            + password + " "
                            + email + " "
                            + AccountManager.getRole(getBaseContext()) + " "
                            + AccountManager.getImage(getBaseContext()));
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }
        customButton.setOnClickListener(new View.OnClickListener() {
            PopupWindow popupWindow;
            @Override
            public void onClick(View view) {
                NumberPicker picker = new NumberPicker(MainActivity.this);
                picker.setMinValue(0);
                picker.setMaxValue(searchOptions.length - 1);
                picker.setDisplayedValues(searchOptions);
                picker.setWrapSelectorWheel(true);

                // Автоматичне оновлення при прокрутці
                picker.setOnValueChangedListener((pickerView, oldVal, newVal) -> {
                    String selected = searchOptions[newVal];
                    Log.e("CustomNumberPicker", "Paint selected " + selected);
                    // Оновлюємо кнопку автоматично
                    customButton.setMessage(selected);

                });


                // Розміщуємо в LinearLayout
                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(40, 40, 40, 40);
                layout.setBackgroundResource(R.drawable.popup_background); // фоновий стиль

                layout.addView(picker);

                popupWindow = new PopupWindow(
                        layout,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true
                );

                popupWindow.showAsDropDown(customButton, 0, -customButton.getHeight() * 3);
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void senRequest() {
        SearchRequest request = new SearchRequest.Builder()
                .setUsername("User id 000")
                .setSearchBy("name")
                .setSearch("albert")
                .build();

        Manager.search(this, request);
    }

    @Override
    public void onSuccess(Object message) {
        try {
            replaceFragment(new FragmentTwo());
            // створюємо відразу JSONArray
            JSONArray responseArray = new JSONArray(message.toString());
            // тепер можеш пройтись по елементах масиву
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject cafeObject = responseArray.getJSONObject(i);
                long id = cafeObject.getLong("id");
                String name = cafeObject.getString("name");
                String address = cafeObject.getString("address");
                String cafeImage = cafeObject.getString("cafe_image");

                Log.e("MainActivity", "Cafe: id=" + id + ", name=" + name);
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }

    }

    @Override
    public void onError(Object message) {

    }

    @Override
    public void messageToActivity(String message) {

    }

    @Override
    public void saveAccount() {

    }
}