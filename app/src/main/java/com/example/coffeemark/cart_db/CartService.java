package com.example.coffeemark.cart_db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.cafe.CafeFound;

import java.util.ArrayList;
import java.util.List;

public class CartService {
    private final DatabaseHelper dbHelper;
    private Context context;

    public CartService(Context context) {
        dbHelper = new DatabaseHelper(context); // this = Activity context
    }

    public boolean isInDatabase(@NonNull CafeFound cafeFound) {
        return dbHelper.findByCart(cafeFound) != null;
    }

    /**
     * Повертаємо карту якщо вона є в базі даних. Якщо немає — повертаємо ту саму, але з позначкою isInDatabase = false.
     */
    public void getCartFromDatabaseAsync(@NonNull CafeFound cafeFound, OnCartLoadedListener listener) {
        new Thread(() -> {
            UserCart userCart = dbHelper.findByCart(cafeFound);

            CafeFound result;
            if (userCart != null) {
                result = new CafeFound.Builder()
                        .setName(userCart.getName())
                        .setAddress(userCart.getAddress())
                        .setCafeImage(userCart.getCafeImage())
                        .setAmountOfCoffee(userCart.getAmountOfCoffee())
                        .setInDatabase(true)
                        .build();
                Log.e("FragmentOne", "Картка вже існує");

            } else {
                result = new CafeFound.Builder()
                        .setName(cafeFound.getName())
                        .setAddress(cafeFound.getAddress())
                        .setCafeImage(cafeFound.getCafeImage())
                        .setAmountOfCoffee(cafeFound.getAmountOfCoffee())
                        .setInDatabase(false)
                        .build();
                Log.e("FragmentOne", "Картка не існує");

            }

            // Повертаємо результат у головний потік
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onCartLoaded(result);
            });
        }).start();
    }

    public void getCartFromDatabaseAsync(@NonNull List<Cafe> cafeList, OnCartLoadedListener listener) {
        new Thread(() -> {
            List<Cafe> newCafeList=new ArrayList<>();
            for (Cafe cafeFound : cafeList) {
                UserCart userCart = dbHelper.findByCart(cafeFound);

                CafeFound result;
                if (userCart != null) {
                    result = new CafeFound.Builder()
                            .setName(userCart.getName())
                            .setAddress(userCart.getAddress())
                            .setCafeImage(userCart.getCafeImage())
                            .setAmountOfCoffee(userCart.getAmountOfCoffee())
                            .setInDatabase(true)
                            .build();
                    Log.e("FragmentOne", "Картка вже існує");

                } else {
                    result = new CafeFound.Builder()
                            .setName(cafeFound.getName())
                            .setAddress(cafeFound.getAddress())
                            .setCafeImage(cafeFound.getCafeImage())
                            .setAmountOfCoffee(0)
                            .setInDatabase(false)
                            .build();
                    Log.e("FragmentOne", "Картка не існує");

                }
                newCafeList.add(result);
            }

            // Повертаємо результат у головний потік
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onCartLoaded(newCafeList);
            });
        }).start();
    }

    public void setCart(@NonNull CafeFound cafeFound) {
        new Thread(() -> {
            UserCart existing = dbHelper.findByCart(cafeFound);

            if (existing != null) {
                Log.e("FragmentOne", "Картка вже існує");
                return;
            }

            UserCart cart = new UserCart.Builder()
                    .setName(cafeFound.getName())
                    .setAddress(cafeFound.getAddress())
                    .setCafeImage(cafeFound.getCafeImage())
                    .setAmountOfCoffee(cafeFound.getAmountOfCoffee())
                    .build();

            dbHelper.setCart(cart);
        }).start();
    }


    public void getCart(Cart cart, int cartId) {
        new Thread(() -> {
            UserCart userCart = dbHelper.getCartById(cartId);
            if (userCart != null) {
                cart.cart(new CafeCart.Builder()
                        .setId(userCart.getId())
                        .setName(userCart.getName())
                        .setAddress(userCart.getAddress())
                        .setCafeImage(userCart.getCafeImage())
                        .setUser_image(AccountManager.getImage(context))
                        .setAmountOfCoffee(userCart.getAmountOfCoffee())
                        .build());
            }
        }).start();
    }

    public void getCarts(Cart cart) {
        Log.e("FragmentOne", "get Cart");
        new Thread(() -> {
            for (UserCart userCart : dbHelper.getAllCarts()) {
                cart.cart(new CafeCart.Builder()
                        .setId(userCart.getId())
                        .setName(userCart.getName())
                        .setAddress(userCart.getAddress())
                        .setCafeImage(userCart.getCafeImage())
                        .setUser_image(AccountManager.getImage(context))
                        .setAmountOfCoffee(userCart.getAmountOfCoffee())
                        .build());
            }
        }).start();
    }

    public interface OnCartLoadedListener {
        void onCartLoaded(CafeFound cart);

        void onCartLoaded(List<Cafe> cafeList);

    }

}
