package com.example.coffeemark.user;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

public class DatabaseHelper {
    private AppDatabase db;
    private UserDao userDao;

    // Міграція з версії 1 на версію 2 (додання нового поля myImage)
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Додаємо нову колонку myImage до таблиці users
            database.execSQL("ALTER TABLE users ADD COLUMN myImage TEXT");
        }
    };

    public DatabaseHelper(Context context) {
        // Ініціалізація Room Database з міграцією
        db = Room.databaseBuilder(context, AppDatabase.class, "coffee_mark_database")
                .addMigrations(MIGRATION_1_2)  // Додаємо міграцію
                .build();
        userDao = db.userDao();  // Отримуємо DAO
    }

    // Додавання користувача в базу даних
    public void setUser(User newUser) {
        new Thread(() -> {
            db.userDao().insertUser(newUser);
        }).start();
    }

    // Метод для зчитування всіх користувачів
    public void getAllUsers() {
        // Операції повинні виконуватись в окремому потоці, так як Room не працює в основному потоці
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<User> users = userDao.getAllUsers();
                // Робіть щось з отриманими даними (наприклад, оновіть UI)
                for (User user : users) {
                    Log.e("User", "Username: " + user.getUsername()+" image: " + user.getMyImage());
                }
            }
        }).start();
    }

    // Метод для зчитування користувача за ID
    public void getUserById(final int userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = userDao.getUserById(userId);
                if (user != null) {
                    Log.d("User", "User found: " + user.getUsername());
                }
            }
        }).start();
    }

    public void deleteAllUsers(){
        new Thread(() -> {
            db.userDao().deleteAllUsers();
            db.userDao().resetAutoIncrement();
        }).start();

    }
}
