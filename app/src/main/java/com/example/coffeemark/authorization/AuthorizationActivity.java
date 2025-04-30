package com.example.coffeemark.authorization;

import static com.example.coffeemark.account.AccountManager.saveAccount;
import static com.example.coffeemark.util.KeyUntil.getPublicKeyHash;
import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;
import static com.example.coffeemark.util.KeyUntil.loadPublicKey;
import static com.example.coffeemark.util.KeyUntil.publicKeyToString;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coffeemark.R;
import com.example.coffeemark.account.Account;
import com.example.coffeemark.dialog.AuthorizationDialog;
import com.example.coffeemark.dialog.ErrorDialog;
import com.example.coffeemark.dialog.StatusHandler;
import com.example.coffeemark.registration.FieldValidator;
import com.example.coffeemark.registration.RegistrationActivity;
import com.example.coffeemark.service.Manager;
import com.example.coffeemark.service.authorization.AuthorizationRequest;
import com.example.coffeemark.service.public_key.LocalPublicKeyRequest;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.util.Encryptor;
import com.example.coffeemark.util.LocalErrorResponse;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CoffeeView;
import com.example.coffeemark.view.CustomButton;

import org.json.JSONObject;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

/**
 * Клас {@code AuthorizationActivity} відповідає за авторизацію користувача.
 * Він дозволяє користувачам ввести свої облікові дані, перевіряє введену інформацію,
 * шифрує дані та надсилає запит на сервер для перевірки облікових даних користувача.
 *
 * <p>Після отримання відповіді від сервера, клас обробляє результат та відображає
 * відповідне повідомлення користувачу. Якщо авторизація успішна, користувач перенаправляється
 * на головний екран програми.</p>
 *
 * <p>Реалізує інтерфейси:
 * <ul>
 *   <li>{@link Manager.MessageAuthorization} — для отримання результату авторизації</li>
 *   <li>{@link AuthorizationDialog.Authorization} — для подальшої обробки після успішної авторизації</li>
 * </ul>
 * </p>
 *
 * @author Ріфат Ісмаїлов
 */

public class AuthorizationActivity extends AppCompatActivity implements Manager.MessageAuthorization, AuthorizationDialog.Authorization, Manager.FileTransferCallback {

    private EditText email, password;
    private CustomButton login, registration;
    private AuthorizationRequest request;
    private CoffeeView coffeeView;
    private String userPassword;
    private String userEmail;
    private PublicKey localPublicKey;
    private PrivateKey localPrivateKey;
    private Account account;
    private final String uuid = UUID.randomUUID().toString();

    /**
     * Відкриває активність реєстрації нового користувача.
     */
    public void startRegistration() {
        Intent serviceIntent = new Intent(this, RegistrationActivity.class);
        startActivity(serviceIntent);
        finish();
    }

    /**
     * Метод викликається при створенні активності.
     * Ініціалізує компоненти інтерфейсу, завантажує публічний ключ, встановлює обробники кнопок.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        registration = findViewById(R.id.register_button);
        coffeeView = findViewById(R.id.image_view);
        coffeeView.setImageResource(R.drawable.emoticon_shame_smiley); // Іконка з емоцією
        localPublicKey = loadPublicKey(this, "user_public.pem");
        localPrivateKey = loadPrivateKey(this, "user_private.pem");
        try {
            PublicKey publicKey = loadPublicKey(this, "public.pem");

            // Клік по кнопці входу
            login.setOnClickListener(view -> {
                registerUser(publicKey);
            });

            // Клік по кнопці реєстрації
            registration.setOnClickListener(view -> startRegistration());

        } catch (Exception e) {
            Log.e("AuthorizationActivity", "Не вдалося завантажити публічний ключ", e);

            // Відображення помилки у вікні
            new ErrorDialog(this, "Authorization", new LocalErrorResponse.Builder().status("1006").message("Неможливо авторизуватися. Спробуйте пізніше.").build()).show();

            login.setEnabled(false);
            registration.setEnabled(false);
        }
    }

    /**
     * Створює об'єкт запиту авторизації, шифрує email і пароль користувача.
     * Валідує введені поля та надсилає дані через Manager.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void registerUser(PublicKey publicKey) {

        userPassword = password.getText().toString();
        userEmail = email.getText().toString();
        boolean isValid = FieldValidator.areFieldsValidAuthorization(userEmail, userPassword);

        if (isValid) {
            try {
                login.onPress("LOGIN");
                request = new AuthorizationRequest.Builder().email(Encryptor.encryptText(userEmail, publicKey)).password(Encryptor.encryptText(userPassword, publicKey)).hash_user_public(getPublicKeyHash(localPublicKey)).uuid(uuid).build();

                Manager.authorization(this, request);

            } catch (Exception e) {
                new ErrorDialog(this, "Authorization", new LocalErrorResponse.Builder().status("1013").message("Помилка під час шифрування.").build()).show();

                Log.e("AuthorizationActivity", "Помилка під час шифрування " + e);
            }

        } else {
            new ErrorDialog(this, "Authorization", new LocalErrorResponse.Builder().status("0000").message("Заповніть будь ласка всі поля.").build()).show();
        }
    }

    /**
     * Метод викликається у разі успішної авторизації.
     * Дешифрує дані, створює об'єкт облікового запису, зберігає його.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSuccess(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            Respond respond = new Respond(jsonObject);

            coffeeView.setImageResource(R.drawable.emoticon_happy);

            String username = Decryptor.decryptText(respond.getUsername(), localPrivateKey);
            String password = Decryptor.decryptText(respond.getPassword(), localPrivateKey);
            String email = Decryptor.decryptText(respond.getEmail(), localPrivateKey);
            String role = respond.getRole();
            String image = Decryptor.decryptText(respond.getImage(), localPrivateKey);

            if (userPassword.equals(password) && userEmail.equals(email)) {
                account = new Account.Builder().username(Encryptor.encryptText(username, localPublicKey)).password(Encryptor.encryptText(userPassword, localPublicKey)).email(Encryptor.encryptText(userEmail, localPublicKey)).role(role).image(image).build();

                saveAccount(this, account);

                if (!"coffee_mark.png".equals(account.getImage())) {
                    Manager.downloadFile(this, image, new ImageHandler(this).getDirFile(image));
                } else {
                    new AuthorizationDialog(this, "Authorization", " авторизація пройшла успішно!").show();
                }
            }
            login.stopLoading();

        } catch (Exception e) {
            Log.e("AuthorizationActivity", "Помилка під час обробки даних з json " + e);
            Log.e("AuthorizationActivity", "String" + message);
        }
        login.stopLoading();
    }

    /**
     * Метод викликається у разі помилки авторизації.
     * Витягує статус і повідомлення, відображає їх користувачу.
     */
    @Override
    public void onFileError(String message) {
        try {
            LocalErrorResponse localErrorResponse = new LocalErrorResponse(new JSONObject(message));
//            if ("1002".equals(localErrorResponse.getStatus()) || "1005".equals(localErrorResponse.getStatus())
//                    || "1006".equals(localErrorResponse.getStatus())) {
//                Manager.setLocalPublicKey(this, new LocalPublicKeyRequest(request, getLocalPublicKey()));
//            }
            new ErrorDialog(this, "Authorization", localErrorResponse).show();
            StatusHandler.handleStatus(localErrorResponse.getStatus(), coffeeView);
        } catch (Exception e) {
            Log.e("AuthorizationActivity", "Помилка під час обробки помилки " + e);
        }
        login.stopLoading();
    }

    /**
     * Отримує локальний публічний ключ користувача у вигляді рядка.
     */
    @Override
    public String getLocalPublicKey() {
        return publicKeyToString(localPublicKey);
    }

    /**
     * Метод викликається після підтвердження авторизації.
     * Надсилає широкомовне повідомлення.
     */
    @Override
    public void continueNext() {
        messageToActivity("Authorization");
        finish();
    }

    // Константи для широкомовного повідомлення
    private static final String ACTION_REGISTRATION_MESSAGE = "com.example.COFFEE_MARK";
    private static final String EXTRA_MESSAGE = "account";

    /**
     * Надсилає широкомовне повідомлення до інших активностей або компонентів програми.
     */
    public void messageToActivity(String message) {
        Intent intent = new Intent(ACTION_REGISTRATION_MESSAGE);
        intent.putExtra(EXTRA_MESSAGE, message);
        sendBroadcast(intent);
    }

    @Override
    public void onProgress(int progress) {
        coffeeView.setProgress(progress);
    }

    @Override
    public void onError(String message) {
        Log.e("AuthorizationActivity", "Authorization onError " + message);
        try {
            LocalErrorResponse localErrorResponse = new LocalErrorResponse(new JSONObject(message));
            if ("1002".equals(localErrorResponse.getStatus()) || "1005".equals(localErrorResponse.getStatus())
                    || "1006".equals(localErrorResponse.getStatus())) {
                Manager.setLocalPublicKey(this, new LocalPublicKeyRequest(request, getLocalPublicKey()));
                Log.e("AuthorizationActivity", "setLocalPublicKey" + getLocalPublicKey());

            } else {
                new ErrorDialog(this, "Authorization", localErrorResponse).show();
            }
            StatusHandler.handleStatus(localErrorResponse.getStatus(), coffeeView);
        } catch (Exception e) {
            Log.e("AuthorizationActivity", "Помилка під час обробки помилки " + e);
        }
        login.stopLoading();

    }


    @Override
    public void onFinish() {
        Log.e("AuthorizationActivity", "onFinish");
        try {
            ImageHandler imageHandler = new ImageHandler(this);
            coffeeView.setImageBitmap(imageHandler.getBitmap(imageHandler.getDirFile(account.getImage())));
        } catch (Exception e) {
            Log.e("AuthorizationActivity", e.toString());
        }
        new AuthorizationDialog(this, "Authorization", " авторизація пройшла успішно!").show();
    }

}
