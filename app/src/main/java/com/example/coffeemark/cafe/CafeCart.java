package com.example.coffeemark.cafe;

public class CafeCart extends CafeBase {

    private int id; // Унікальний ідентифікатор кожного запису (автоінкремент)
    private final String user_image;
    private final int amount_of_coffee;

    public int getId() {
        return id;
    }

    public int getAmount_of_coffee() {
        return amount_of_coffee;
    }

    public String getUser_image() {
        return user_image;
    }

    private CafeCart(Builder builder) {

        super(builder.name, builder.address, builder.cafe_image);
        this.id = builder.id;
        this.user_image = builder.user_image;
        this.amount_of_coffee = builder.amount_of_coffee;
    }

    // Builder class
    public static class Builder {
        private int id;
        private String name;
        private String address;
        private String cafe_image;
        private String user_image;
        private int amount_of_coffee;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

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

        public CafeCart build() {
            return new CafeCart(this);
        }
    }
}
