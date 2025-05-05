package com.example.coffeemark.cafe;


public class CafeFound extends CafeBase {
    private final String user_image;
    private int amount_of_coffee;
    private boolean isInDatabase; // поле, яке вказує чи обʼєкт в базі

    private CafeFound(Builder builder) {
        super(builder.name, builder.address, builder.cafe_image);
        this.user_image = builder.user_image;
        this.amount_of_coffee = builder.amount_of_coffee;
        this.isInDatabase = builder.isInDatabase;
    }

    public String getUser_image() {
        return user_image;
    }

    public int getAmountOfCoffee() {
        return amount_of_coffee;
    }

    public boolean isInDatabase() {
        return isInDatabase;
    }

    // Builder class
    public static class Builder {
        private String name;
        private String address;
        private String cafe_image;
        private String user_image;
        private int amount_of_coffee;
        private boolean isInDatabase;

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

        public Builder setUser_image(String user_image) {
            this.user_image = user_image;
            return this;
        }

        public Builder setAmountOfCoffee(int amount_of_coffee) {
            this.amount_of_coffee = amount_of_coffee;
            return this;
        }

        public Builder setInDatabase(boolean inDatabase) {
            isInDatabase = inDatabase;
            return this;
        }

        public CafeFound build() {
            return new CafeFound(this);
        }
    }
}

