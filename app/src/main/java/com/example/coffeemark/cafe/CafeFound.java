package com.example.coffeemark.cafe;

public class CafeFound extends CafeBase {

    private int amount_of_coffee;
    public CafeFound(String name, String address, String cafe_image, int amount_of_coffee) {
        super(name, address, cafe_image);
        this.amount_of_coffee = amount_of_coffee;
    }

    public int getAmount_of_coffee() {
        return amount_of_coffee;
    }

    public void setAmount_of_coffee(int amount_of_coffee) {
        this.amount_of_coffee = amount_of_coffee;
    }
}
