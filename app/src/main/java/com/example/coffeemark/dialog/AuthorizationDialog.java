package com.example.coffeemark.dialog;

import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.coffeemark.R;
import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.view.CoffeeView;

import java.security.PrivateKey;

/**
 * Клас {@code AuthorizationDialog} відповідає за створення діалогу авторизації користувача.
 * Цей діалог відображає результат авторизації користувача, зокрема ім'я користувача, email
 * та повідомлення про успішну авторизацію. Діалог також надає кнопку для переходу до наступного етапу.
 * <p>
 * Діалог забороняє закриття, поки користувач не натисне кнопку "Продовжити". Після цього буде викликано метод
 * {@link Authorization#continueNext()} для переходу до наступного етапу та закриття діалогу.
 * </p>
 * <p>
 * Реалізує інтерфейс {@link Authorization}, що дозволяє обробити подальші дії після авторизації.
 *
 * @author Ріфат Ісмаїлов
 */
public class AuthorizationDialog extends Dialog {
    private final String clasName;
    private final Authorization authorization;
    private final String message;

    /**
     * Конструктор класу {@code AuthorizationDialog}.
     *
     * @param context  контекст, в якому створюється діалог.
     * @param clasName назва класу (наприклад, назва користувача або інша інформація),
     *                 яку буде показано в діалозі.
     */
    public AuthorizationDialog(@NonNull Context context, String clasName, String message) {
        super(context);
        authorization = (Authorization) context;
        this.clasName = clasName;
        this.message = message;
    }

    /**
     * Метод {@code onCreate} ініціалізує елементи інтерфейсу та налаштовує поведінку діалогу.
     * У цьому методі встановлюється прозорий фон для діалогу, а також відображаються дані
     * користувача (наприклад, ім'я та email), що були отримані через дешифрування.
     * <p>
     * Також у методі {@code onCreate} забороняється закриття діалогу, поки не буде натиснута кнопка
     * "Продовжити". Після натискання цієї кнопки буде викликано метод {@link Authorization#continueNext()},
     * що дозволить виконати подальші дії.
     *
     * @param savedInstanceState збережений стан діалогу, використовується для відновлення попередніх налаштувань.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Забираємо заголовок діалогу
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);

        // Прозорий фон для закруглених країв
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        // Доступ до елементів
        CoffeeView coffeeView = findViewById(R.id.imageError);
        TextView name = findViewById(R.id.clasName);
        TextView titleText = findViewById(R.id.titleText);
        TextView messageText = findViewById(R.id.messageText);
        TextView dismissBtn = findViewById(R.id.dismissButton);
        name.setText(clasName);
        PrivateKey privateKey = loadPrivateKey(getContext(), "user_private.pem");

        try {
            String username = Decryptor.decryptText(AccountManager.getUsername(getContext()), privateKey);
            String email = Decryptor.decryptText(AccountManager.getEmail(getContext()), privateKey);
            titleText.setText(email);
            messageText.setText(username + message);
            coffeeView.setImageResource(R.drawable.emoticon_glas_smiley);
            dismissBtn.setText("Продовжити");
        } catch (Exception e) {
            Log.e("AuthorizationDialog", e.toString());
        }

        // Запобігаємо закриттю діалогу до натискання кнопки "Продовжити"
        setCancelable(false);

        dismissBtn.setOnClickListener(v -> {
            authorization.continueNext();
            dismiss(); // Закриває діалог після натискання кнопки
        });
    }

    /**
     * Інтерфейс {@code Authorization} визначає метод {@code continueNext()},
     * який викликається після натискання кнопки "Продовжити" в діалозі.
     * <p>
     * Цей метод використовується для виконання наступних дій після успішної авторизації.
     */
    public interface Authorization {
        /**
         * Метод, що викликається після натискання кнопки "Продовжити" в діалозі.
         * Використовується для переходу до наступного етапу після авторизації.
         */
        void continueNext();
    }
}

