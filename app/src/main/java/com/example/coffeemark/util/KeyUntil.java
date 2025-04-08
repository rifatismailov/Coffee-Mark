package com.example.coffeemark.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class KeyUntil {
    private static final String TEST = "coffee mark";

    /**
     * Перевіряє локальні ключі користувача (публічний та приватний).
     * Якщо хоча б один з них відсутній або пари ключів не співпадають — генерує нову пару.
     *
     * @param context Контекст додатку або активності, що використовується для доступу до внутрішнього сховища.
     */
    public static void checkLocalKey(Context context) {
        try {
            // Якщо хоча б один з ключів відсутній, генеруємо нові
            if (!isKeyExists(context, "user_public.pem") || !isKeyExists(context, "user_private.pem")) {
                generateKeys(context);  // Генерація нових ключів
                Log.e("KeyUntil", "Генерація нових ключів");

            } else {
                // Якщо ключі існують — перевіряємо, чи вони утворюють правильну пару
                if (!keysMatch(context)) {
                    // Якщо ключі не співпадають — генеруємо нові
                    Log.e("KeyUntil", "Ключі не співпадають — генеруємо нові");

                    generateKeys(context);
                }
            }
        } catch (Exception e) {
            // У разі помилки при перевірці або генерації — кидаємо виняток з поясненням
            throw new RuntimeException("Помилка генерації ключів", e);
        }
    }

    /**
     * Перевіряє, чи існує файл з ключем у внутрішньому сховищі додатку.
     *
     * @param context  Контекст додатку для доступу до файлової системи.
     * @param filename Назва файлу, який перевіряється (наприклад, "user_public.pem").
     * @return true, якщо файл існує; false — якщо файл відсутній.
     */
    public static boolean isKeyExists(Context context, String filename) {
        File file = new File(context.getFilesDir(), filename);
        return file.exists(); // true означає "файл існує"
    }


    /**
     * Перевіряє, чи приватний і публічний ключі є валідною парою.
     * Метод шифрує тестовий текст публічним ключем і розшифровує приватним,
     * потім порівнює результат із початковим текстом.
     *
     * @param context Контекст додатку для доступу до файлової системи.
     * @return true, якщо ключі є валідною парою; false — якщо виникла помилка або результат не співпадає.
     */
    private static boolean keysMatch(Context context) {
        try {
            // Завантаження публічного і приватного ключів із файлів
            PublicKey publicKey = loadPublicKey(context, "user_public.pem");
            PrivateKey privateKey = loadPrivateKey(context, "user_private.pem");

            // Тестове повідомлення (має бути константою десь у класі, наприклад: private static final String TEST = "test";)
            String encryptedText = Encryptor.encryptText(TEST, publicKey);
            String decryptedText = Decryptor.decryptText(encryptedText, privateKey);

            // Порівняння розшифрованого тексту з оригіналом
            return decryptedText.equals(TEST);

        } catch (Exception e) {
            // У разі помилки повертаємо false — ключі не співпадають
            return false;
        }
    }

    /**
     * Генерує нову пару RSA ключів (публічний і приватний), кодує їх у формат PEM
     * і зберігає у внутрішньому сховищі додатку.
     *
     * @param context Контекст додатку для збереження ключів у файлову систему.
     * @throws Exception Якщо виникла помилка під час генерації або збереження ключів.
     */
    private static void generateKeys(Context context) throws Exception {
        // Ініціалізація генератора ключів для алгоритму RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Довжина ключа: 2048 біти

        // Генерація ключової пари
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Кодування публічного ключа у формат PEM
        String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) +
                "\n-----END PUBLIC KEY-----";

        // Кодування приватного ключа у формат PEM
        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
                "\n-----END PRIVATE KEY-----";

        // Збереження обох ключів у локальні файли
        saveKey("user_public.pem", publicKeyPem, context);
        saveKey("user_private.pem", privateKeyPem, context);
    }


    /**
     * Перетворює публічний ключ у формат PEM як рядок.
     *
     * @param publicKey Публічний ключ, який потрібно перетворити.
     * @return PEM-форматований рядок, що містить публічний ключ.
     *         Формат виглядатиме як:
     *         -----BEGIN PUBLIC KEY-----
     *         (Base64 закодований ключ)
     *         -----END PUBLIC KEY-----
     */
    public static String publicKeyToString(PublicKey publicKey) {
        // Отримуємо байти ключа
        byte[] keyBytes = publicKey.getEncoded();

        // Кодуємо в Base64
        String base64PublicKey = Base64.getEncoder().encodeToString(keyBytes);

        // Формуємо PEM-формат
        return "-----BEGIN PUBLIC KEY-----\n" +
                base64PublicKey +
                "\n-----END PUBLIC KEY-----";
    }

    /**
     * Завантажує публічний ключ із файлу у внутрішньому сховищі.
     *
     * @param context       Контекст додатку для доступу до файлової системи.
     * @param publicKeyFile Назва файлу, в якому зберігається публічний ключ (наприклад, "user_public.pem").
     * @return Публічний ключ, зчитаний із файлу.
     * @throws RuntimeException Якщо виникає помилка при завантаженні публічного ключа.
     */
    public static PublicKey loadPublicKey(Context context, String publicKeyFile) {
        try {
            // Зчитуємо байти публічного ключа з файлу
            byte[] publicBytes = readKeyBytes(context, publicKeyFile);
            // Створюємо об'єкт KeyFactory для генерування публічного ключа
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(publicBytes);
            return keyFactory.generatePublic(pubSpec);
        } catch (Exception e) {
            // Генеруємо виняток при помилці завантаження ключа
            throw new RuntimeException("Помилка завантаження публічного ключа", e);
        }
    }

    /**
     * Завантажує приватний ключ із файлу у внутрішньому сховищі.
     *
     * @param context         Контекст додатку для доступу до файлової системи.
     * @param privateKeyFile  Назва файлу, в якому зберігається приватний ключ (наприклад, "user_private.pem").
     * @return Приватний ключ, зчитаний із файлу.
     * @throws RuntimeException Якщо виникає помилка при завантаженні приватного ключа.
     */
    public static PrivateKey loadPrivateKey(Context context, String privateKeyFile) {
        try {
            // Зчитуємо байти приватного ключа з файлу
            byte[] privateBytes = readKeyBytes(context, privateKeyFile);
            // Створюємо об'єкт KeyFactory для генерування приватного ключа
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privateBytes);
            return keyFactory.generatePrivate(privSpec);
        } catch (Exception e) {
            // Генеруємо виняток при помилці завантаження ключа
            throw new RuntimeException("Помилка завантаження приватного ключа", e);
        }
    }

    /**
     * Зчитує байти ключа з файлу у внутрішньому сховищі додатку.
     * Видаляє "PEM-формат" (початкові та кінцеві мітки, пробіли) та повертає ключ у вигляді байтів.
     *
     * @param context  Контекст додатку для доступу до файлової системи.
     * @param filename Назва файлу, з якого зчитується ключ.
     * @return Масив байтів, що містить ключ.
     * @throws IOException Якщо виникає помилка при зчитуванні файлу.
     */
    private static byte[] readKeyBytes(Context context, String filename) throws IOException {
        InputStream is = context.openFileInput(filename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;

        // Зчитуємо вміст файлу
        while ((read = is.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }

        // Видаляємо PEM-маркування і пробіли
        String key = bos.toString(StandardCharsets.UTF_8.name())
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "") // Видаляє всі пробіли, переноси рядків тощо
                .trim();

        // Декодуємо з Base64 і повертаємо байти
        return Base64.getDecoder().decode(key);
    }

    /**
     * Зберігає ключ у вигляді PEM-формату у файл в внутрішньому сховищі.
     *
     * @param name   Назва файлу, в який зберігається ключ.
     * @param pem    PEM-форматований ключ у вигляді рядка.
     * @param context Контекст додатку для доступу до файлової системи.
     * @throws IOException Якщо виникає помилка при записі файлу.
     */
    public static void saveKey(String name, String pem, Context context) throws IOException {
        // Отримуємо потік для запису в файл у внутрішньому сховищі
        FileOutputStream fos = context.openFileOutput(name, Context.MODE_PRIVATE);
        // Записуємо ключ у файл
        fos.write(pem.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    /**
     * Обчислює хеш публічного ключа за допомогою алгоритму SHA-256.
     *
     * @param publicKey Публічний ключ, для якого потрібно обчислити хеш.
     * @return Хеш публічного ключа у вигляді рядка в шістнадцятковому форматі.
     * @throws NoSuchAlgorithmException Якщо алгоритм SHA-256 не підтримується.
     */
    public static String getPublicKeyHash(PublicKey publicKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(publicKey.getEncoded());

        // Перетворення байтів у hex-рядок
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
