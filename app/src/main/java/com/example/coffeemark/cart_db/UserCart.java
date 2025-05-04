package com.example.coffeemark.cart_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.example.coffeemark.cafe.CafeBase;
import com.example.coffeemark.cafe.CafeFound;

/**
 * Клас представляє сутність таблиці `cart` у базі даних Room.
 * Він наслідує CafeBase, але дублює основні поля з анотаціями,
 * щоб Room міг працювати з ними (Room не підтримує наслідування з полями).
 */

@Entity(
        tableName = "cart",
        indices = {@Index(value = {"name", "address"}, unique = true)}
)
public class UserCart extends CafeBase {

    @PrimaryKey(autoGenerate = true)
    private int id; // Унікальний ідентифікатор кожного запису (автоінкремент)

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "cafe_image")
    private String cafe_image;

    @ColumnInfo(name = "amount_of_coffee")
    private int amount_of_coffee;

    /**
     * Конструктор для створення нового запису UserCart.
     */
    public UserCart(String name, String address, String cafe_image, int amount_of_coffee) {
        super(name, address, cafe_image); // виклик конструктора CafeBase
        this.amount_of_coffee = amount_of_coffee;
    }

    // Геттери та сеттери для Room

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCafe_image() {
        return cafe_image;
    }

    public int getAmount_of_coffee() {
        return amount_of_coffee;
    }
    private UserCart(Builder builder) {
        super(builder.name, builder.address, builder.cafe_image);
        this.amount_of_coffee = builder.amount_of_coffee;
    }

    public int getAmountOfCoffee() {
        return amount_of_coffee;
    }



    // Builder class
    public static class Builder {
        private String name;
        private String address;
        private String cafe_image;
        private int amount_of_coffee;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setCafeImage(String cafe_image) {
            this.cafe_image = cafe_image;
            return this;
        }

        public Builder setAmountOfCoffee(int amount_of_coffee) {
            this.amount_of_coffee = amount_of_coffee;
            return this;
        }

        public UserCart build() {
            return new UserCart(this);
        }
    }
}

