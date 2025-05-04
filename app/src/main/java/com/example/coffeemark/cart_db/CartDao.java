package com.example.coffeemark.cart_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

/**
 * DAO (Data Access Object) для доступу до таблиці `cart`.
 * Містить методи для виконання SQL-запитів.
 */
@Dao
public interface CartDao {

    /**
     * Вставляє новий обʼєкт UserCart у базу.
     */
    @Insert
    void insertUser(UserCart cart);

    /**
     * Повертає список усіх карт у базі.
     */
    @Query("SELECT * FROM cart")
    List<UserCart> getAllCarts();

    /**
     * Повертає одну карту за ID.
     */
    @Query("SELECT * FROM cart WHERE id = :cartsId")
    UserCart getCartById(int cartsId);

    /**
     * Видаляє всі записи з таблиці `cart`.
     */
    @Query("DELETE FROM cart")
    void deleteAllCarts();

    /**
     * Скидає автоінкремент ідентифікатора `id`.
     * (очищення значення у внутрішній таблиці sqlite_sequence)
     */
    @Query("DELETE FROM sqlite_sequence WHERE name = 'cart'")
    void resetAutoIncrement();

    @Query("SELECT * FROM cart WHERE name = :name AND address = :address LIMIT 1")
    UserCart findByNameAndAddress(String name, String address);

}
