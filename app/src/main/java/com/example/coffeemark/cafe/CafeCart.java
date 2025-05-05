package com.example.coffeemark.cafe;

import java.util.Objects;

/**
 * Клас представляє збережену кав'ярню, яка знаходиться в базі даних.
 * Наслідує {@link CafeBase}, додає поля ідентифікатора, кількості кави та зображення користувача.
 */
public class CafeCart extends CafeBase {

    /** Унікальний ідентифікатор кавʼярні в базі (автоінкремент). */
    private int id;

    /** Зображення користувача, пов'язане з цією кавʼярнею. */
    private final String user_image;

    /** Кількість випитої або замовленої кави. */
    private final int amount_of_coffee;

    /**
     * Повертає ідентифікатор об'єкта з бази.
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Повертає кількість кави.
     * @return кількість кави
     */
    public int getAmount_of_coffee() {
        return amount_of_coffee;
    }

    /**
     * Повертає шлях або URI до зображення користувача.
     * @return шлях до зображення
     */
    public String getUser_image() {
        return user_image;
    }

    /**
     * Приватний конструктор, який створюється через {@link Builder}.
     * @param builder екземпляр будівника
     */
    private CafeCart(Builder builder) {
        super(builder.name, builder.address, builder.cafe_image);
        this.id = builder.id;
        this.user_image = builder.user_image;
        this.amount_of_coffee = builder.amount_of_coffee;
    }

    /**
     * Патерн Builder для зручного створення екземплярів {@link CafeCart}.
     */
    public static class Builder {
        private int id;
        private String name;
        private String address;
        private String cafe_image;
        private String user_image;
        private int amount_of_coffee;

        /**
         * Встановлює ID об'єкта.
         * @param id ідентифікатор
         * @return Builder
         */
        public Builder setId(int id) {
            this.id = id;
            return this;
        }

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
         * @param user_image зображення
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
         * Будує фінальний об'єкт {@link CafeCart}.
         * @return екземпляр CafeCart
         */
        public CafeCart build() {
            return new CafeCart(this);
        }
    }

    /**
     * Перевизначений метод для логічного порівняння об'єктів {@link CafeCart}.
     * @param obj інший об'єкт
     * @return true, якщо рівні
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CafeCart that = (CafeCart) obj;

        return id == that.id &&
                amount_of_coffee == that.amount_of_coffee &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getCafeImage(), that.getCafeImage()) &&
                Objects.equals(getUser_image(), that.getUser_image());
    }

    /**
     * Обчислює хеш-код для об'єкта {@link CafeCart}.
     * @return значення хеш-коду
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, getName(), getAddress(), getCafeImage(), getUser_image(), amount_of_coffee);
    }
}
