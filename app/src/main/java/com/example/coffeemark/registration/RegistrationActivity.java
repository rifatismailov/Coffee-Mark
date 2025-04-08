package com.example.coffeemark.registration;

import static com.example.coffeemark.util.KeyUntil.loadPublicKey;
import static com.example.coffeemark.util.KeyUntil.publicKeyToString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.account.Account;
import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.registration.cafe.Cafe;
import com.example.coffeemark.registration.cafe.CafeAdapter;
import com.example.coffeemark.service.Manager;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.uploader.Uploader;
import com.example.coffeemark.util.Encryptor;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.util.UrlBuilder;
import com.example.coffeemark.view.CoffeeView;
import com.example.coffeemark.view.CustomButton;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас {@code RegistrationActivity} відповідає за реєстрацію нового користувача.
 * Він забезпечує інтерфейс для введення персональних даних користувача, таких як ім'я користувача,
 * електронна пошта, пароль, роль (наприклад, BARISTA), а також дозволяє додавати інформацію
 * про кав'ярні та завантажувати зображення.
 *
 * <p>У випадку вибору ролі BARISTA, користувач може додати декілька кав'ярень із назвою,
 * адресою та зображенням. Зображення шифрується та завантажується на сервер.</p>
 *
 * <p>Після перевірки полів, дані шифруються за допомогою відкритого ключа та надсилаються на сервер
 * для реєстрації.</p>
 *
 * <p>Реалізує інтерфейси:
 * <ul>
 *   <li>{@link Uploader.Operation} — для обробки результату завантаження зображень</li>
 *   <li>{@link Manager.ManagerRegistration} — для обробки результату реєстрації користувача</li>
 * </ul>
 * </p>
 *
 * @author Ріфат Ісмаїлов
 */

public class RegistrationActivity extends AppCompatActivity implements Uploader.Operation, Manager.ManagerRegistration {

    /**
     * Кастомне зображення кавової іконки.
     */
    private CoffeeView coffeeView;

    /**
     * Поле для введення імені користувача.
     */
    private EditText username;

    /**
     * Поле для введення паролю.
     */
    private EditText password;

    /**
     * Поле для введення електронної пошти.
     */
    private EditText email;

    /**
     * Поле для введення назви кавʼярні (для BARISTA).
     */
    private EditText cafeName;

    /**
     * Поле для введення адреси кавʼярні.
     */
    private EditText cafeAddress;

    /**
     * Spinner для вибору ролі користувача.
     */
    private Spinner roleSpinner;

    /**
     * Layout, що містить поля для додавання кавʼярні.
     */
    private LinearLayout cafeLayout;

    /**
     * Кнопка для додавання кавʼярні до списку.
     */
    private Button addCafeButton;

    /**
     * Кастомна кнопка для виконання реєстрації.
     */
    private CustomButton registerButton;

    /**
     * Список кавʼярень, які додає користувач.
     */
    private final List<Cafe> cafeList = new ArrayList<>();

    /**
     * Адаптер для відображення списку кавʼярень.
     */
    private CafeAdapter adapter;

    /**
     * Назва зображення, обране користувачем або за замовчуванням.
     */
    private String image;

    /**
     * Обробник зображень (вибір, обробка, збереження).
     */
    private ImageHandler imageHandler;

    /**
     * Публічний ключ сервера для шифрування публічної інформації.
     */
    private PublicKey publicKey;

    /**
     * Локальний публічний ключ користувача для шифрування приватної інформації.
     */
    private PublicKey localPublicKey;

    /**
     * Об’єкт облікового запису, який буде створено після реєстрації.
     */
    private Account account;

    /**
     * Метод {@code onCreate()} ініціалізує всі елементи інтерфейсу, обробники подій,
     * а також логіку шифрування, адаптерів та завантаження зображень.
     *
     * @param savedInstanceState збережений стан активності.
     */
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);//— це спосіб зробити ваш UI більш адаптивним, гнучким та "повноекранним".
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Прив'язка елементів інтерфейсу
        coffeeView = findViewById(R.id.coffee_view);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        roleSpinner = findViewById(R.id.roleSpinner);
        cafeLayout = findViewById(R.id.cafe_layout);
        cafeName = findViewById(R.id.cafeName);
        cafeAddress = findViewById(R.id.cafeAddress);
        addCafeButton = findViewById(R.id.add_cafe_button);
        registerButton = findViewById(R.id.register_button);
        coffeeView.setImageResource(R.drawable.emoticon_shame_smiley);
        RecyclerView recyclerView = findViewById(R.id.cafeList);
        // Завантаження публічних ключів
        publicKey = loadPublicKey(this, "public.pem");
        localPublicKey = loadPublicKey(this, "user_public.pem");

        // Ініціалізація обробника зображень
        imageHandler = new ImageHandler(this);

        // Ініціалізація RecyclerView для кавʼярень
        adapter = new CafeAdapter(cafeList, imageHandler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = parent.getItemAtPosition(position).toString();
                if (selectedRole.equals("BARISTA")) {
                    cafeLayout.setVisibility(View.VISIBLE);
                    addCafeButton.setVisibility(View.VISIBLE);
                } else {
                    cafeLayout.setVisibility(View.GONE);
                    addCafeButton.setVisibility(View.GONE);
                    cafeList.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addCafeButton.setOnClickListener(v -> {
            String name = cafeName.getText().toString();
            String address = cafeAddress.getText().toString();
            if (!name.isEmpty() && !address.isEmpty()) {
                try {
                    String imageCafe = image != null ? image : "coffee_mark.png";

                    cafeList.add(new Cafe(name, address, Encryptor.encryptText(imageCafe, publicKey)));
                    adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView

                    cafeName.setText("");
                    cafeAddress.setText("");
                    Toast.makeText(this, "Кав'ярня додана", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("RegisterActivity", e.toString());
                }

            }
        });

        registerButton.setOnClickListener(v -> {
            try {
                registerUser();
            } catch (Exception e) {
                Log.e("RegisterActivity", e.toString());
            }
        });
        coffeeView.setOnClickListener(view -> openGallery());
    }

    /**
     * Метод для реєстрації користувача. Збирає введені дані, шифрує та відправляє на сервер.
     *
     * @throws Exception у разі помилки шифрування
     */
    private void registerUser() throws Exception {


        account = new Account.Builder()
                .username(Encryptor.encryptText(username.getText().toString(), localPublicKey))
                .password(Encryptor.encryptText(password.getText().toString(), localPublicKey))
                .email(Encryptor.encryptText(email.getText().toString(), localPublicKey))
                .role(roleSpinner.getSelectedItem().toString())
                .image(image != null ? image : "coffee_mark.png")
                .build();
        boolean isValid = FieldValidator.areFieldsValidRegistration(
                username.getText().toString(),
                password.getText().toString(),
                email.getText().toString(),
                roleSpinner.getSelectedItem().toString());

        if (isValid) {

            registerButton.onPress("Please wait");

            RegisterRequest request = new RegisterRequest.Builder()
                    .username(Encryptor.encryptText(username.getText().toString(), publicKey))
                    .password(Encryptor.encryptText(password.getText().toString(), publicKey))
                    .email(Encryptor.encryptText(email.getText().toString(), publicKey))
                    .role(roleSpinner.getSelectedItem().toString())
                    .cafes(roleSpinner.getSelectedItem().toString().equals("BARISTA") ? cafeList : null) //публічна іфномарція
                    .image(Encryptor.encryptText(account.getImage(), publicKey)) // може бути пріватна інфомарція
                    .public_key(publicKeyToString(localPublicKey))
                    .build();
            Manager.registration(this, request);
        } else {
            // Поля не заповнені
            Toast.makeText(RegistrationActivity.this, "Заповніть буд ласка всі поля", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Відкриває галерею для вибору зображення користувача.
     */
    private void openGallery() {
        // Потім просто викликаєш:
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    /**
     * Обробник результатів вибору зображення.
     */
    ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    handleImageUri(imageUri);
                    // Тут передаєш URI у свою функцію для обробки
                }
            }
    );


    /**
     * Обробляє вибране зображення, зберігає та оновлює інтерфейс.
     *
     * @param uri URI зображення, вибраного користувачем
     */
    private void handleImageUri(Uri uri) {
        try {
            String serverUrl = new UrlBuilder.Builder()
                    .setProtocol("http")
                    .setIp("192.168.177.4")
                    .setPort("8020")
                    .setDirectory("/api/files/upload")
                    .build()
                    .buildUrl();


            coffeeView.setImageBitmap(imageHandler.getBitmap(uri));

            File savedFile = imageHandler.processAndSaveImage(uri);
            image = imageHandler.getSavedFileName();
            if (!cafeList.isEmpty()) {
                for (Cafe cafe : cafeList) {
                    cafe.setCafe_image(image);
                }
                adapter.notifyDataSetChanged(); // Оновлюємо весь список
            }
            Log.e("RegisterActivity", "Saved to: " + savedFile.getAbsolutePath());

            new Uploader(this, serverUrl).uploadFile(savedFile);
        } catch (IOException e) {
            Log.e("RegisterActivity", "Image processing failed", e);
        }
    }

    /**
     * Викликається після успішної реєстрації/завантаження даних.
     *
     * @param message текст успіху
     */
    @Override
    public void onSuccess(String message) {
        registerButton.stopLoading();
        registerButton.setMessage(message);
        AccountManager.saveAccount(this, account);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Обробка помилок під час реєстрації.
     *
     * @param message повідомлення про помилку
     */
    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        registerButton.stopLoading();
        registerButton.setMessage("REGISTER");
    }

    private static final String ACTION_REGISTRATION_MESSAGE = "com.example.COFFEE_MARK";
    private static final String EXTRA_MESSAGE = "account";

    /**
     * Передає повідомлення про результат реєстрації іншій активності.
     *
     * @param message текст повідомлення
     */
    @Override
    public void messageToActivity(String message) {
        Intent intent = new Intent(ACTION_REGISTRATION_MESSAGE);
        intent.putExtra(EXTRA_MESSAGE, message);
        sendBroadcast(intent);  // Надсилання повідомлення Activity 1
    }

    /**
     * Зберігає акаунт користувача у локальне сховище.
     */
    @Override
    public void saveAccount() {
        AccountManager.saveAccount(this, account);
    }

    /**
     * Встановлює прогрес виконання.
     *
     * @param progress відсоток завершення
     * @param info     додаткова інформація
     */
    @Override
    public void setProgress(int progress, String info) {
        Log.e("RegisterActivity", progress + " " + info);
    }

    /**
     * Завершення процесу реєстрації/завантаження.
     *
     * @param positionId ID позиції
     * @param info       повідомлення про завершення
     */
    @Override
    public void endProgress(String positionId, String info) {
        Log.e("RegisterActivity", positionId + " " + info);
    }


}
