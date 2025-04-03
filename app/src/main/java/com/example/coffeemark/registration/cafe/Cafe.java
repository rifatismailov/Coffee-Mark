package com.example.coffeemark.registration.cafe;

public class Cafe {

    private String name;
    private String address;

    public Cafe(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
