package com.example.coffeemark;

import static com.example.coffeemark.service.Manager.checkPublicKey;
import static com.example.coffeemark.util.KeyUntil.checkLocalKey;
import static com.example.coffeemark.util.KeyUntil.getPublicKeyHash;
import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.authorization.AuthorizationActivity;


import com.example.coffeemark.fragment.FragmentOne;
import com.example.coffeemark.cafe.CafeBase;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.service.Manager;
import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.search.SearchRequest;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.util.Encryptor;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CustomButton;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Manager.ManagerSearch{

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

    /**
     * Список кавʼярень, які додає користувач.
     */
    private final List<CafeBase> cafeList = new ArrayList<>();

    /**
     * Адаптер для відображення списку кавʼярень.
     */
    private CafeAdapter adapter;

    private ImageHandler imageHandler;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        CustomButton customButton=findViewById(R.id.request_button);
        //RecyclerView recyclerView = findViewById(R.id.mainCafeList);
//        ViewPager2 viewPager = findViewById(R.id.viewPager);
//        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setPadding(0, 0, 0, 100); // Збільшення відступу знизу
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//
//        imageHandler = new ImageHandler(this);
//        adapter = new CafeAdapter(cafeList, imageHandler);
//        viewPager.setAdapter(adapter);
//
//        CompositePageTransformer transformer = new CompositePageTransformer();
//        transformer.addTransformer(new MarginPageTransformer(40));
//        transformer.addTransformer((page, position) -> {
//            // Налаштування масштабу елементів
//            float r = 1 - Math.abs(position);
//            page.setScaleY(0.85f + r * 0.15f); // Зменшуємо картки позаду
//        });
//        viewPager.setPageTransformer(transformer);
//


        // Ініціалізація RecyclerView для кавʼярень
//        imageHandler = new ImageHandler(this);
//        adapter = new CafeAdapter(cafeList, imageHandler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
        replaceFragment(new FragmentOne());
        checkLocalKey(this);
        checkPublicKey(this);

        // new DatabaseHelper(this).deleteAllUsers();
        //registerRegistrationActivity();
        startRegistration();
//        for (int i = 0; i < 10; i++) {
//            cafeList.add(new CafeCart("name cafe "+i, "address cafe "+i, "coffee_mark.png",4));
//            adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView
//        }

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
            @Override
            public void onClick(View view) {
                senRequest();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    public void senRequest(){
        SearchRequest request = new SearchRequest.Builder()
                .setUsername("User id 000")
                .setSearchBy("Name Cafe")
                .setSearch("Cafe Cafe Cafe")
                .build();

        Manager.search(this, request);
    }

    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void messageToActivity(String message) {

    }

    @Override
    public void saveAccount() {

    }
}