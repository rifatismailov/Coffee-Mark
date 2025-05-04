package com.example.coffeemark.cart_db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Головний клас бази даних Room.
 * Room генерує реалізацію цього класу автоматично.
 */
@Database(entities = {UserCart.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Отримання доступу до DAO.
     */
    public abstract CartDao cartDao();
}
