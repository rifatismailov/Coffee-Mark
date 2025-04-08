package com.example.coffeemark.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.coffeemark.R;

import com.example.coffeemark.util.LocalErrorResponse;
import com.example.coffeemark.view.CoffeeView;

public class ErrorDialog extends Dialog {
    private final LocalErrorResponse localErrorResponse;
    private String clasName;

    public ErrorDialog(@NonNull Context context, String clasName, LocalErrorResponse localErrorResponse) {
        super(context);
        this.clasName = clasName;
        this.localErrorResponse = localErrorResponse;
    }

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
        titleText.setText(localErrorResponse.getStatus());
        messageText.setText(localErrorResponse.getMessage());

        StatusHandler.handleStatus(localErrorResponse.getStatus(), coffeeView);

        dismissBtn.setOnClickListener(v -> dismiss());
    }
}
