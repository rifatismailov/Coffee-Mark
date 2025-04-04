package com.example.coffeemark.registration.cafe;

public class Cafe {

    private final String name;
    private final String address;
    private final String cafe_image;

    public Cafe(String name, String address,String cafe_image) {
        this.name = name;
        this.address = address;
        this.cafe_image = cafe_image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCafe_image() {
        return cafe_image;
    }
}
