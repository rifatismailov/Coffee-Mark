package com.example.coffeemark.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO (Data Access Object) для роботи з базою даних
 */

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);  // Для додавання користувача

    @Query("SELECT * FROM users")  // Запит для зчитування всіх користувачів
    List<User> getAllUsers();  // Повертає список користувачів

    @Query("SELECT * FROM users WHERE id = :userId")  // Запит для пошуку користувача за ID
    User getUserById(int userId);
}


