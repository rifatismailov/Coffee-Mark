package com.example.coffeemark.registration;

import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;
import static com.example.coffeemark.util.KeyUntil.loadPublicKey;
import static com.example.coffeemark.util.KeyUntil.publicKeyToString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.account.Account;
import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.authorization.Respond;
import com.example.coffeemark.dialog.AuthorizationDialog;
import com.example.coffeemark.dialog.ErrorDialog;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.cafe.CafeShop;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.service.Manager;
import com.example.coffeemark.service.registration.RegisterRequest;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.util.Encryptor;
import com.example.coffeemark.util.LocalErrorResponse;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CoffeeView;
import com.example.coffeemark.view.CustomButton;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
 *   <li>{@link Manager.ManagerRegistration} — для обробки результату реєстрації користувача</li>
 * </ul>
 * </p>
 *
 * @author Ріфат Ісмаїлов
 */

public class RegistrationActivity extends AppCompatActivity implements Manager.FileTransferCallback,
        Manager.ManagerRegistration, AuthorizationDialog.Authorization,CafeAdapter.OnItemClickListener {

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
    private final List<Cafe> cafeShopList_for_Server = new ArrayList<>();
    private final List<Cafe> cafeShopList_for_App = new ArrayList<>();


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
     * Локальний приватний ключ користувача для розшифрування приватної інформації.
     */
    private PrivateKey localPrivateKey;

    /**
     * Об’єкт облікового запису, для тимчасового зберігання.
     */
    private Registration registration;


    /**
     * Метод {@code onCreate()} ініціалізує всі елементи інтерфейсу, обробники подій,
     * а також логіку шифрування, адаптерів та завантаження зображень.
     *
     * @param savedInstanceState збережений стан активності.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        localPrivateKey = loadPrivateKey(this, "user_private.pem");
        Log.e("RegisterActivity", publicKey.toString());

        // Ініціалізація обробника зображень
        imageHandler = new ImageHandler(this);

        // Ініціалізація RecyclerView для кавʼярень
        adapter = new CafeAdapter(cafeShopList_for_App, imageHandler,this);
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
                    cafeShopList_for_Server.clear();
                    cafeShopList_for_App.clear();
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

                    cafeShopList_for_Server.add(new CafeShop(name, address, Encryptor.encryptText(imageCafe, publicKey)));
                    cafeShopList_for_App.add(new CafeShop(name, address, imageCafe));
                    adapter.notifyItemInserted(cafeShopList_for_App.size() - 1); // Оновлюємо RecyclerView

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
                Log.e("RegisterActivity", "registerUser "+e.toString());
            }
        });
        coffeeView.setOnClickListener(view -> openGallery());
    }

    /**
     * Метод для реєстрації користувача. Збирає введені дані, шифрує та відправляє на сервер.
     *
     * @throws Exception у разі помилки шифрування
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser() throws Exception {
        String uuid = UUID.randomUUID().toString();
        // Створення об'єкта registration
        registration = new Registration.Builder()
                .username(username.getText().toString())
                .password(password.getText().toString())
                .email(email.getText().toString())
                .role(roleSpinner.getSelectedItem().toString())
                .image(image != null ? image : "coffee_mark.png")
                .uuid(uuid)
                .build();

        // Перевірка валідності полів
        boolean isValid = FieldValidator.areFieldsValidRegistration(
                username.getText().toString(),
                password.getText().toString(),
                email.getText().toString(),
                roleSpinner.getSelectedItem().toString());

        if (isValid) {

            // Налаштовуємо кнопку на "Please wait"
            registerButton.onPress("Please wait");

            // Створюємо список кафе, який буде передано в запит
            List<Cafe> cafes = roleSpinner.getSelectedItem().toString().equals("BARISTA") ? cafeShopList_for_Server : new ArrayList<>();

            // Створення об'єкта RegisterRequest
            RegisterRequest request = new RegisterRequest.Builder()
                    .username(Encryptor.encryptText(username.getText().toString(), publicKey))
                    .password(Encryptor.encryptText(password.getText().toString(), publicKey))
                    .email(Encryptor.encryptText(email.getText().toString(), publicKey))
                    .role(roleSpinner.getSelectedItem().toString())
                    .cafes(cafes) // передаємо список кафе (порожній список, якщо не "BARISTA")
                    .image(Encryptor.encryptText(registration.getImage(), publicKey)) // може бути пріватна інфомарція
                    .public_key(publicKeyToString(localPublicKey))
                    .uuid(registration.getUuid())
                    .build();

            // Відправка запиту на реєстрацію
            Manager.registration(this, request);

        } else {
            // Якщо поля не заповнені
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

            coffeeView.setImageBitmap(imageHandler.getBitmap(uri));

            File savedFile = imageHandler.processAndSaveImage(uri);
            image = imageHandler.getSavedFileName();
            if (!cafeShopList_for_Server.isEmpty()) {
                for (Cafe cafe : cafeShopList_for_Server) {
                    cafe.setCafeImage(image);
                }
                adapter.notifyDataSetChanged(); // Оновлюємо весь список
            }
            Log.e("RegisterActivity", "Saved to: " + savedFile.getAbsolutePath());

            Manager.uploadFile(savedFile, this);
        } catch (IOException e) {
            Log.e("RegisterActivity", "Image processing failed", e);
        }
    }

    /**
     * Викликається після успішної реєстрації/завантаження даних.
     *
     * @param message текст успіху
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(String message) {
        registerButton.stopLoading();
        try {

            JSONObject jsonObject = new JSONObject(message);
            Respond respond = new Respond(jsonObject);

            Log.e("RegisterActivity", "jsonObject "+jsonObject);
            Log.e("RegisterActivity", "localPrivateKey "+localPrivateKey);

            // Дешифрування даних з respond
            String usernameFromRespond = Decryptor.decryptText(respond.getUsername(), localPrivateKey);
            Log.e("RegisterActivity", "usernameFromRespond "+usernameFromRespond);

            String passwordFromRespond = Decryptor.decryptText(respond.getPassword(), localPrivateKey);
            Log.e("RegisterActivity", "passwordFromRespond "+passwordFromRespond);

            String emailFromRespond = Decryptor.decryptText(respond.getEmail(), localPrivateKey);
            Log.e("RegisterActivity", "emailFromRespond "+emailFromRespond);

            String roleFromRespond = respond.getRole();
            String imageFromRespond = Decryptor.decryptText(respond.getImage(), localPrivateKey);
            Log.e("RegisterActivity", "imageFromRespond "+imageFromRespond);

            Log.e("RegisterActivity", "AccountManager "+AccountManager.getImage(this));

            if (!"coffee_mark.png".equals(imageFromRespond)) {
                ImageHandler imageHandler = new ImageHandler(this);
                coffeeView.setImageBitmap(imageHandler.getBitmap(imageHandler.getDirFile(imageFromRespond)));
            } else {
                coffeeView.setImageResource(R.drawable.emoticon_glas_smiley);
            }

            // Отримуємо дані з registration
            String usernameFromRegistration = registration.getUsername();
            Log.e("RegisterActivity", "usernameFromRegistration "+usernameFromRegistration);

            String passwordFromRegistration = registration.getPassword();
            Log.e("RegisterActivity", "passwordFromRegistration "+passwordFromRegistration);

            String emailFromRegistration = registration.getEmail();
            Log.e("RegisterActivity", "emailFromRegistration "+emailFromRegistration);

            String roleFromRegistration = registration.getRole();
            Log.e("RegisterActivity", "roleFromRegistration "+roleFromRegistration);

            String imageFromRegistration = registration.getImage();
            Log.e("RegisterActivity", "imageFromRegistration "+imageFromRegistration);


            // Перевірка чи дані з registration співпадають з respond
            boolean isDataEqual = usernameFromRespond.equals(usernameFromRegistration) &&
                    passwordFromRespond.equals(passwordFromRegistration) &&
                    emailFromRespond.equals(emailFromRegistration) &&
                    roleFromRespond.equals(roleFromRegistration) &&
                    imageFromRespond.equals(imageFromRegistration);

            // Якщо всі дані співпадають, можна продовжувати з обробкою
            if (isDataEqual) {
                Account account = new Account.Builder()
                        .username(Encryptor.encryptText(usernameFromRegistration, localPublicKey))
                        .password(Encryptor.encryptText(passwordFromRegistration, localPublicKey))
                        .email(Encryptor.encryptText(emailFromRegistration, localPublicKey))
                        .role(roleFromRegistration)
                        .image(imageFromRegistration)
                        .build();
                AccountManager.saveAccount(this, account);
                new AuthorizationDialog(this, "Registration", " регістрація пройшла успішно!").show();

            } else {
                // Обробка помилки, якщо дані не співпадають
                Log.e("RegisterActivity", "Дані не співпадають з отриманими з respond");
                // Відображення помилки у вікні
                new ErrorDialog(this, "Authorization", new LocalErrorResponse.Builder()
                        .status("0000")
                        .message("Дані не співпадають з отриманими з respond")
                        .build()).show();
            }

        } catch (Exception e) {
            Log.e("RegisterActivity", e.toString());
            new ErrorDialog(this, "Authorization", new LocalErrorResponse.Builder()
                    .status("0000")
                    .message("Помилка під час отримання данних.")
                    .build()).show();
        }
    }

    /**
     * Обробка помилок під час реєстрації.
     *
     * @param message повідомлення про помилку
     */
    @Override
    public void onFileError(String message) {
        Log.e("RegisterActivity", message);

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
        //AccountManager.saveAccount(this, account);
    }

    /**
     * Встановлює прогрес виконання.
     *
     * @param progress відсоток завершення
     */
    @Override
    public void onProgress(int progress) {
        coffeeView.setProgress(progress); // Установлює прогрес у 50%
    }

    @Override
    public void onError(String e) {
        Log.e("RegisterActivity", "UploaderError " + e);
    }

    /**
     * Завершення процесу реєстрації/завантаження.
     */
    @Override
    public void onFinish() {
        Log.e("RegisterActivity", "UploaderFinis");
    }


    @Override
    public void continueNext() {
        messageToActivity("Registration");
        finish();
    }

    @Override
    public void onItemClick(Cafe model) {

    }
}
