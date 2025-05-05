package com.example.coffeemark.cafe;

/**
 * Клас представляє кавʼярню, яку було знайдено під час пошуку (але ще не обовʼязково збережену у базі).
 * Наслідує {@link CafeBase} і розширюється додатковими полями: зображенням користувача, кількістю кави,
 * та прапором, який вказує, чи вже ця кавʼярня знаходиться в базі даних.
 */
public class CafeFound extends CafeBase {

    /** Шлях до зображення користувача (локальне чи мережеве посилання). */
    private final String user_image;

    /** Кількість кави, яку випито чи привʼязано до цієї кавʼярні. */
    private int amount_of_coffee;

    /** Прапор, який вказує, чи вже ця кавʼярня присутня в базі даних. */
    private boolean isInDatabase;

    /**
     * Приватний конструктор, який створюється лише через {@link Builder}.
     * @param builder Екземпляр Builder для ініціалізації полів
     */
    private CafeFound(Builder builder) {
        super(builder.name, builder.address, builder.cafe_image);
        this.user_image = builder.user_image;
        this.amount_of_coffee = builder.amount_of_coffee;
        this.isInDatabase = builder.isInDatabase;
    }

    /**
     * Повертає шлях до зображення користувача.
     * @return зображення користувача
     */
    public String getUser_image() {
        return user_image;
    }

    /**
     * Повертає кількість кави.
     * @return кількість кави
     */
    public int getAmountOfCoffee() {
        return amount_of_coffee;
    }

    /**
     * Повертає, чи кавʼярня вже збережена в базі даних.
     * @return true, якщо в базі
     */
    public boolean isInDatabase() {
        return isInDatabase;
    }

    /**
     * Патерн Builder для зручного створення екземплярів {@link CafeFound}.
     */
    public static class Builder {
        private String name;
        private String address;
        private String cafe_image;
        private String user_image;
        private int amount_of_coffee;
        private boolean isInDatabase;

        /**
         * Встановлює назву кавʼярні.
         * @param name назва
         * @return Builder
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Встановлює адресу кавʼярні.
         * @param address адреса
         * @return Builder
         */
        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        /**
         * Встановлює зображення кавʼярні.
         * @param cafe_image зображення
         * @return Builder
         */
        public Builder setCafeImage(String cafe_image) {
            this.cafe_image = cafe_image;
            return this;
        }

        /**
         * Встановлює зображення користувача.
         * @param user_image зображення користувача
         * @return Builder
         */
        public Builder setUser_image(String user_image) {
            this.user_image = user_image;
            return this;
        }

        /**
         * Встановлює кількість кави.
         * @param amount_of_coffee кількість
         * @return Builder
         */
        public Builder setAmountOfCoffee(int amount_of_coffee) {
            this.amount_of_coffee = amount_of_coffee;
            return this;
        }

        /**
         * Встановлює прапор, чи ця кавʼярня є в базі.
         * @param inDatabase true — якщо є
         * @return Builder
         */
        public Builder setInDatabase(boolean inDatabase) {
            isInDatabase = inDatabase;
            return this;
        }

        /**
         * Будує фінальний об'єкт {@link CafeFound}.
         * @return екземпляр CafeFound
         */
        public CafeFound build() {
            return new CafeFound(this);
        }
    }
}
