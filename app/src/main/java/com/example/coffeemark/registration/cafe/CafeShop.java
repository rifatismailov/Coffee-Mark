package com.example.coffeemark.registration.cafe;

public class CafeShop implements CafeBase{

    private final String name;
    private final String address;
    private String cafe_image;

    public CafeShop(String name, String address,String cafe_image) {
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

    @Override
    public String getCafeImage() {
        return cafe_image;
    }

    @Override
    public void setCafeImage(String image) {
        this.cafe_image = cafe_image;
    }


}
