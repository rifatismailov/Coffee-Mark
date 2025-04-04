package com.example.coffeemark.user;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 2, exportSchema = false)  // Вимикає експорт схеми
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

