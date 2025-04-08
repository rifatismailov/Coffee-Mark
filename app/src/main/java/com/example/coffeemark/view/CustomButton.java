package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import com.example.coffeemark.R;

public class CustomButton extends AppCompatButton {

    private String message; // Основний текст кнопки
    private int cornerRadius; // Радіус кутів
    private int buttonColor;
    private int borderColor;
    private int borderWidth; // Ширина рамки
    private boolean buttonProgress;

    private Handler handler = new Handler();
    private boolean isAnimating = false; // Стан анімації
    private int dotCount = 0; // Лічильник крапок

    public CustomButton(Context context) {
        super(context);
        init(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final android.content.res.TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.CustomButton, 0, 0);

            try {
                buttonColor = a.getColor(R.styleable.CustomButton_buttonColor, Color.BLUE);
                borderColor = a.getColor(R.styleable.CustomButton_borderColor, Color.BLACK);
                cornerRadius = a.getDimensionPixelSize(R.styleable.CustomButton_buttonRadius, 5);
                borderWidth = a.getDimensionPixelSize(R.styleable.CustomButton_borderWidth, 2);
                message = a.getString(R.styleable.CustomButton_buttonText);
                buttonProgress = a.getBoolean(R.styleable.CustomButton_buttonProgress, false);
            } finally {
                a.recycle();
            }
        }

        updateBackground();

        if (message != null) {
            setText(message);
        }
    }

    private void updateBackground() {
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(buttonColor);
        backgroundDrawable.setCornerRadius(cornerRadius);
        backgroundDrawable.setStroke(borderWidth, borderColor);
        setBackground(backgroundDrawable);
    }

    public void setMessage(String message) {
        setText(message); // Повертає стандартний текст

    }

    // Метод для натискання кнопки (запускає анімацію)
    public void onPress(String responseMessage) {
        startLoading(responseMessage);
    }

    // Запускає анімацію трьох крапок
    private void startLoading(String text) {
        stopLoading();
        message = text;
        isAnimating = true;
        dotCount = 0;
        if (buttonProgress) {
            handler.postDelayed(loadingRunnable, 500);
        }
    }

    // Зупиняє анімацію
    public void stopLoading() {
        isAnimating = false;
        if (buttonProgress) handler.removeCallbacks(loadingRunnable);
        setText(message); // Повертає стандартний текст
    }

    // Runnable для зміни тексту з трьома крапками
    private final Runnable loadingRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAnimating) return;

            dotCount = (dotCount + 1) % 4; // Лічильник крапок (0-3)
            String dots = new String(new char[dotCount]).replace("\0", "."); // Генерація крапок
            setText(message + dots);

            handler.postDelayed(this, 500);
        }
    };
}
