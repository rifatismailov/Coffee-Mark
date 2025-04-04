package com.example.coffeemark.user;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import java.util.List;

public class DatabaseHelper {
    private AppDatabase db;
    private UserDao userDao;

    public DatabaseHelper(Context context) {
        // Ініціалізація Room Database
        db = Room.databaseBuilder(context, AppDatabase.class, "coffee_mark_database").build();
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
                    Log.e("User", "Username: " + user.getUsername());
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
}

