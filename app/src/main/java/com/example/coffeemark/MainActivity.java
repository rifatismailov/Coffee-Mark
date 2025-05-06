package com.example.coffeemark;

import static com.example.coffeemark.service.manager.KeyManager.checkPublicKey;
import static com.example.coffeemark.util.KeyUntil.checkLocalKey;
import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.authorization.AuthorizationActivity;


import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.cafe.CafeFound;
import com.example.coffeemark.cart_db.CartService;
import com.example.coffeemark.fragment.FragmentOne;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.fragment.FragmentTwo;
import com.example.coffeemark.option.ScrollPickerAdapter;
import com.example.coffeemark.search.CenterSnapScrollListener;
import com.example.coffeemark.search.Watcher;
import com.example.coffeemark.service.manager.SearchManager;
import com.example.coffeemark.service.search.SearchRequest;
import com.example.coffeemark.util.Decryptor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchManager.Search, CartService.Service.OnCartLoadedListListener, FragmentTwo.OnCartListener, Watcher.OnWatcher {

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

    private EditText searchInput;
    private FragmentTwo fragmentTwo;
    private String username;
    private String password;
    private String email;
    private CartService cartService;
    private String search = "name";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        checkLocalKey(this);
        checkPublicKey(this);
        cartService = new CartService(this);
        searchInput = findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new Watcher(this));

        // new DatabaseHelper(this).deleteAllUsers();
        //registerRegistrationActivity();
        //startRegistration();
        fragmentTwo = new FragmentTwo(this);
        replaceFragment(new FragmentOne());
        RecyclerView scrollPicker = findViewById(R.id.scroll_picker);
        scrollPicker.setLayoutManager(new LinearLayoutManager(this));

        String[] searchOptions = {"Name", "Address", "Barista", "Coffee"};
        ScrollPickerAdapter adapter = new ScrollPickerAdapter(searchOptions, selected -> {
            Log.e("MainActivity", "Обрано після прокрутки: " + selected);
            search = selected.equals("search") ? "name" : selected;
        });
        scrollPicker.setAdapter(adapter);

        // Додаємо SnapHelper
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(scrollPicker);

        // Визначаємо обраний елемент після прокрутки
        scrollPicker.addOnScrollListener(new CenterSnapScrollListener(
                scrollPicker,
                snapHelper,
                position -> adapter.selectPosition(position),
                400 // затримка в мілісекундах
        ));


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

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    private final List<Cafe> cafeList = new ArrayList<>();

    @Override
    public void onSuccess(Object message) {
        try {
            // створюємо відразу JSONArray

            JSONArray responseArray = new JSONArray(message.toString());
            cafeList.clear();
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject cafeObject = responseArray.getJSONObject(i);
                long id = cafeObject.getLong("id");
                cafeList.add(new CafeFound.Builder()
                        .setName(cafeObject.getString("name"))
                        .setAddress(cafeObject.getString("address"))
                        .setCafeImage(cafeObject.getString("cafe_image"))
                        .setAmountOfCoffee(0)
                        .build());
            }

            cartService.getCartFromDatabaseAsync(cafeList, this);

        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }

    }

    @Override
    public void onError(Object message) {

    }

    @Override
    public void onCartLoaded(List<Cafe> cafeList) {
        runOnUiThread(() -> fragmentTwo.showSearch(cafeList));
    }

    @Override
    public void onItemClick(CafeCart cart) {
        replaceFragment(new FragmentOne(cart));
    }

    @Override
    public void isEditEmpty() {
        cafeList.clear();
        replaceFragment(new FragmentOne());
    }

    @Override
    public void isEditFull(String text) {
        replaceFragment(fragmentTwo);
        runOnUiThread(() -> {
            SearchRequest request = new SearchRequest.Builder()
                    .setUsername(username)
                    .setSearchBy(search)
                    .setSearch(text)
                    .build();

            SearchManager.search(this, request);
        });

    }

    @Override
    public EditText isEdit() {
        return searchInput;
    }
}