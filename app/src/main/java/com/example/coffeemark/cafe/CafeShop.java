package com.example.coffeemark.cafe;

/**
 * Представляє кавʼярню, яка показується як "звичайна" (наприклад, без додаткових полів).
 * Наслідує {@link CafeBase} і використовується переважно для демонстрації або списку.
 */
public class CafeShop extends CafeBase {

    /**
     * Створює новий екземпляр {@link CafeShop}.
     * @param name назва кавʼярні
     * @param address адреса кавʼярні
     * @param cafe_image зображення кавʼярні
     */
    public CafeShop(String name, String address, String cafe_image) {
        super(name, address, cafe_image);
    }
}
