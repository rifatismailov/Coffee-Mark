package com.example.coffeemark.cafe;

public class CafeBase implements Cafe {
    private final String name;
    private final String address;
    private String cafe_image;

    public CafeBase(String name, String address, String cafe_image) {
        this.name = name;
        this.address = address;
        this.cafe_image = cafe_image;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
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
}
