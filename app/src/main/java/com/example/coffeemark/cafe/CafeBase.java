package com.example.coffeemark.cafe;

/**
 * Абстрактний базовий клас для всіх моделей кавʼярень.
 * Реалізує інтерфейс {@link Cafe}, і містить спільні поля:
 * назву, адресу, та зображення кавʼярні.
 */
public class CafeBase implements Cafe {

    /** Назва кавʼярні */
    private final String name;

    /** Адреса кавʼярні */
    private final String address;

    /** Зображення кавʼярні (може бути URL або локальний файл) */
    private String cafe_image;

    /**
     * Конструктор для ініціалізації базової кавʼярні.
     * @param name назва кавʼярні
     * @param address адреса кавʼярні
     * @param cafe_image зображення кавʼярні
     */
    public CafeBase(String name, String address, String cafe_image) {
        this.name = name;
        this.address = address;
        this.cafe_image = cafe_image;
    }

    /**
     * Повертає назву кавʼярні.
     * @return назва
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Повертає адресу кавʼярні.
     * @return адреса
     */
    @Override
    public String getAddress() {
        return address;
    }

    /**
     * Повертає зображення кавʼярні.
     * @return шлях до зображення
     */
    @Override
    public String getCafeImage() {
        return cafe_image;
    }

    /**
     * Встановлює нове зображення кавʼярні.
     * @param image нове зображення
     */
    @Override
    public void setCafeImage(String image) {
        this.cafe_image = image;
    }
}
