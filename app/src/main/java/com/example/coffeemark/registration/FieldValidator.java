package com.example.coffeemark.registration;

public class FieldValidator {

    /**
     * Метод для перевірки, чи заповнені всі необхідні поля.
     *
     * @param user - текст користувача
     * @param pass - пароль
     * @param mail - email
     * @param role - роль
     * @return true, якщо всі поля заповнені, false - якщо хоча б одне порожнє
     */
    public static boolean areFieldsValidRegistration(String user, String pass, String mail, String role) {
        // Перевіряємо, чи всі поля не порожні
        if (user.isEmpty() || pass.isEmpty() || mail.isEmpty() || role.isEmpty()) {
            return false; // Якщо хоча б одне поле порожнє, повертаємо false
        }
        return true; // Якщо всі поля заповнені, повертаємо true
    }
    /**
     * Метод для перевірки, чи заповнені всі необхідні поля.
     *
     * @param mail - email
     * @param pass - пароль
     * @return true, якщо всі поля заповнені, false - якщо хоча б одне порожнє
     */
    public static boolean areFieldsValidAuthorization(String mail, String pass) {
        // Перевіряємо, чи всі поля не порожні
        if (mail.isEmpty() || pass.isEmpty()) {
            return false; // Якщо хоча б одне поле порожнє, повертаємо false
        }
        return true; // Якщо всі поля заповнені, повертаємо true
    }
}
