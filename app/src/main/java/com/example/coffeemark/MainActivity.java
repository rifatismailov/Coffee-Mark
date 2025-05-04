package com.example.coffeemark;

import static com.example.coffeemark.service.Manager.checkPublicKey;
import static com.example.coffeemark.util.KeyUntil.checkLocalKey;
import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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


import com.example.coffeemark.cafe.CafeFound;
import com.example.coffeemark.fragment.FragmentOne;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.fragment.FragmentTwo;
import com.example.coffeemark.service.Manager;
import com.example.coffeemark.service.search.SearchRequest;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.view.CustomButton;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private CustomButton customButton;
    private EditText searchInput;
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private String username;
    private String password;
    private String email;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkLocalKey(this);
        checkPublicKey(this);
        customButton = findViewById(R.id.request_button);
        searchInput = findViewById(R.id.search_input);
        setupSearch(searchInput);

        // Дані для вибору
        String[] searchOptions = {"Name", "Address", "Barista", "Coffee"};

        // new DatabaseHelper(this).deleteAllUsers();
        //registerRegistrationActivity();
        startRegistration();
        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();
        replaceFragment(fragmentOne);

        try {
            PrivateKey privateKey = loadPrivateKey(getBaseContext(), "user_private.pem");
            username = Decryptor.decryptText(AccountManager.getUsername(getBaseContext()), privateKey);
            password = Decryptor.decryptText(AccountManager.getPassword(getBaseContext()), privateKey);
            email = Decryptor.decryptText(AccountManager.getEmail(getBaseContext()), privateKey);

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
                // Явно повертаємо клавіатуру
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void senRequest() {

    }

    private final List<Cafe> cafeList = new ArrayList<>();

    @Override
    public void onSuccess(Object message) {
        try {
            // створюємо відразу JSONArray

            JSONArray responseArray = new JSONArray(message.toString());
            cafeList.clear();
            // тепер можеш пройтись по елементах масиву
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject cafeObject = responseArray.getJSONObject(i);
                long id = cafeObject.getLong("id");
                cafeList.add(new CafeFound.Builder()
                        .setName(cafeObject.getString("name"))
                        .setAddress(cafeObject.getString("address"))
                        .setCafeImage(cafeObject.getString("cafe_image"))
                        .setAmountOfCoffee(4)
                        .setInDatabase(false)
                        .build());
            }
            for (Cafe fCafeCart : cafeList) {
                Log.e("MainActivity", "name " + fCafeCart.getName() + " address " + fCafeCart.getAddress());
            }
            fragmentTwo.showSearch(cafeList);
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

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DELAY_MS = 100; // затримка 500мс

    private void setupSearch(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Скасовуємо попередній запуск
                handler.removeCallbacks(searchRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String query = s.toString().trim();

                searchRunnable = () -> {
                    if (query.isEmpty() || editText.getSelectionStart() == 0) {
                        cafeList.clear();
                        replaceFragment(fragmentOne);
                    } else {
                        if (!query.startsWith(" ")) {
                            setSearch(query);
                            replaceFragment(fragmentTwo);
                        }
                    }
                };

                handler.postDelayed(searchRunnable, DELAY_MS);
            }
        });
    }


    public void setSearch(String text) {
        try {
            runOnUiThread(() -> {
                String search = customButton.getText().toString().equals("search") ? "name" : customButton.getText().toString();
                SearchRequest request = new SearchRequest.Builder()
                        .setUsername(username)
                        .setSearchBy(search)
                        .setSearch(text)
                        .build();

                Manager.search(this, request);

            });
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());

        }
    }
}