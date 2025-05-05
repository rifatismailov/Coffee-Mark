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

/**
 * Сервіс, який відповідає за збереження, пошук та отримання даних про кавʼярні в базі Room.
 * Всі методи працюють у фонових потоках, щоб уникати блокування UI.
 */
public class CartService {

    private final DatabaseHelper dbHelper;
    private final Context context;

    /**
     * Ініціалізація сервісу.
     * @param context Контекст Activity або Application
     */
    public CartService(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    /**
     * Перевіряє, чи існує передана кавʼярня в базі.
     * @param cafeFound Об'єкт для перевірки
     * @return true, якщо існує
     */
    public boolean isInDatabase(@NonNull CafeFound cafeFound) {
        return dbHelper.findByCart(cafeFound) != null;
    }

    /**
     * Повертає `CafeCart`, якщо знайдено запис у базі, або null.
     * @param cart Кавʼярня, яку шукаємо
     * @return CafeCart або null
     */
    public CafeCart isReturnInDatabase(@NonNull Cafe cart) {
        UserCart userCart = dbHelper.findByCart(cart);
        if (userCart != null) {
            return new CafeCart.Builder()
                    .setId(userCart.getId())
                    .setName(userCart.getName())
                    .setAddress(userCart.getAddress())
                    .setCafeImage(userCart.getCafeImage())
                    .setUser_image(AccountManager.getImage(context))
                    .setAmountOfCoffee(userCart.getAmountOfCoffee())
                    .build();
        }
        return null;
    }

    /**
     * Асинхронно перевіряє наявність кожної кавʼярні у списку.
     * Повертає оновлений список із прапором isInDatabase.
     * @param cafeList список кавʼярень
     * @param listener обробник результату
     */
    public void getCartFromDatabaseAsync(@NonNull List<Cafe> cafeList, Service.OnCartLoadedListListener listener) {
        new Thread(() -> {
            List<Cafe> copyList = new ArrayList<>(cafeList);
            List<Cafe> newCafeList = new ArrayList<>();

            for (Cafe cafeFound : copyList) {
                UserCart userCart = dbHelper.findByCart(cafeFound);
                CafeFound result = (userCart != null)
                        ? new CafeFound.Builder()
                        .setName(userCart.getName())
                        .setAddress(userCart.getAddress())
                        .setCafeImage(userCart.getCafeImage())
                        .setUser_image(AccountManager.getImage(context))
                        .setAmountOfCoffee(userCart.getAmountOfCoffee())
                        .setInDatabase(true)
                        .build()
                        : new CafeFound.Builder()
                        .setName(cafeFound.getName())
                        .setAddress(cafeFound.getAddress())
                        .setCafeImage(cafeFound.getCafeImage())
                        .setUser_image(AccountManager.getImage(context))
                        .setAmountOfCoffee(0)
                        .setInDatabase(false)
                        .build();

                newCafeList.add(result);
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onCartLoaded(newCafeList);
            });
        }).start();
    }

    /**
     * Додає кавʼярню в базу, якщо її там ще немає.
     * @param cafeFound Кавʼярня для збереження
     */
    public void setCart(@NonNull CafeFound cafeFound) {
        new Thread(() -> {
            UserCart existing = dbHelper.findByCart(cafeFound);
            if (existing != null) return;

            UserCart cart = new UserCart.Builder()
                    .setName(cafeFound.getName())
                    .setAddress(cafeFound.getAddress())
                    .setCafeImage(cafeFound.getCafeImage())
                    .setAmountOfCoffee(cafeFound.getAmountOfCoffee())
                    .build();

            dbHelper.setCart(cart);
        }).start();
    }

    /**
     * Отримує одну картку за ID і повертає її через інтерфейс.
     * @param cart інтерфейс-отримувач
     * @param cartId ID картки
     */
    public void getCart(Service.Cart cart, int cartId) {
        new Thread(() -> {
            UserCart userCart = dbHelper.getCartById(cartId);
            if (userCart != null) {
                cart.showCart(new CafeCart.Builder()
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

    /**
     * Отримує всі картки з бази і повертає їх одну за одною через колбек.
     * @param cart інтерфейс-отримувач
     */
    public void getCarts(Service.Cart cart) {
        new Thread(() -> {
            for (UserCart userCart : dbHelper.getAllCarts()) {
                cart.showCart(new CafeCart.Builder()
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

    /**
     * Контракт для колбеків.
     */
    public abstract static class Service {

        /** Колбек для повернення однієї картки */
        public interface Cart {
            void showCart(CafeCart cart);
        }

        /** Колбек для повернення списку оновлених кавʼярень */
        public interface OnCartLoadedListListener {
            void onCartLoaded(List<Cafe> cafeList);
        }

        /** Колбек для однієї кавʼярні */
        public interface OnCartLoadedListener {
            void onCartLoaded(Cafe cafe);
        }
    }
}
