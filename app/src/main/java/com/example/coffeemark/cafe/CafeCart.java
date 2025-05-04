package com.example.coffeemark.cafe;

public class CafeCart implements CafeBase{

    private final String name;
    private final String address;
    private String cafe_image;
    private String user_image;

    private int amount_of_coffee;

    public CafeCart(String name, String address, String cafe_image) {
        this.name = name;
        this.address = address;
        this.cafe_image = cafe_image;
    }

    public CafeCart(String name, String address, String cafe_image, int amount_of_coffee) {
        this.name = name;
        this.address = address;
        this.cafe_image = cafe_image;
        this.amount_of_coffee = amount_of_coffee;
    }
    public CafeCart(String name, String address, String cafe_image,String user_image, int amount_of_coffee) {
        this.name = name;
        this.address = address;
        this.cafe_image = cafe_image;
        this.user_image=user_image;
        this.amount_of_coffee = amount_of_coffee;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getCafeImage() {
        return cafe_image;
    }

    @Override
    public void setCafeImage(String image) {
        this.cafe_image = cafe_image;
    }

    public int getAmount_of_coffee() {
        return amount_of_coffee;
    }

    public String getUser_image() {
        return user_image;
    }
}
