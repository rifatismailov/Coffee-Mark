package com.example.coffeemark.cart_db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.coffeemark.cafe.Cafe;

import java.util.List;

/**
 * Клас-допоміжник для ініціалізації Room, доступу до DAO та виконання операцій із базою.
 * Усі виклики Room виконуються в окремому потоці.
 */
public class DatabaseHelper {

    private final AppDatabase db;
    private final CartDao cartDao;

    /**
     * Приклад міграції бази даних з версії 2 до 3.
     * Додає нове поле myImage (не використовується у цьому прикладі).
     */
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE cart ADD COLUMN myImage TEXT");
        }
    };

    /**
     * Ініціалізація бази даних Room.
     */
    public DatabaseHelper(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "coffee_mark_database")
                .fallbackToDestructiveMigration() // ❗ Видаляє стару БД при зміні схеми
                .build();
//        Room.databaseBuilder(context, AppDatabase.class, "coffee_mark_database")
//                .addMigrations(MIGRATION_2_3)
//                .build();
        cartDao = db.cartDao();
    }
    public UserCart findByCart(Cafe cafe){
        return db.cartDao().findByNameAndAddress(cafe.getName(), cafe.getAddress());
    }

    /**
     * Додає новий запис (карту) у базу даних.
     */
    public void setCart(UserCart newCart) {
            db.cartDao().insertUser(newCart);
    }

    /**
     * Зчитує всі записи (карти) з бази та виводить у лог.
     */
    public List<UserCart>  getAllCarts() {
        return cartDao.getAllCarts();
    }

    /**
     * Отримує конкретну карту за ID та виводить у лог.
     */
    public UserCart getCartById(int cartId) {
            return cartDao.getCartById(cartId);
    }

    /**
     * Видаляє всі карти з бази та скидає автоінкремент ID.
     */
    public void deleteAllUsers() {
        new Thread(() -> {
            db.cartDao().deleteAllCarts();
            db.cartDao().resetAutoIncrement();
        }).start();
    }
}
