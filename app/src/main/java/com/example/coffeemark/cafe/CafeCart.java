package com.example.coffeemark.cafe;

public class CafeCart extends CafeBase {


    private String user_image;
    private int amount_of_coffee;

    public CafeCart(String name, String address, String cafe_image) {
        super(name, address, cafe_image);
    }

    public CafeCart(String name, String address, String cafe_image, int amount_of_coffee) {
        super(name, address, cafe_image);
        this.amount_of_coffee = amount_of_coffee;
    }

    public CafeCart(String name, String address, String cafe_image, String user_image, int amount_of_coffee) {
        super(name, address, cafe_image);
        this.user_image = user_image;
        this.amount_of_coffee = amount_of_coffee;
    }

    public int getAmount_of_coffee() {
        return amount_of_coffee;
    }

    public String getUser_image() {
        return user_image;
    }
}
