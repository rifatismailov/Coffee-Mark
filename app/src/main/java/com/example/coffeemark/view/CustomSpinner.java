package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import com.example.coffeemark.R;

public class CustomSpinner extends AppCompatSpinner {

    private String message; // Основний текст кнопки
    private int cornerRadius; // Радіус кутів
    private int buttonColor;
    private int borderColor;
    private int borderWidth; // Ширина рамки
    private boolean buttonProgress;

    private Handler handler = new Handler();
    private boolean isAnimating = false; // Стан анімації
    private int dotCount = 0; // Лічильник крапок

    public CustomSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
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

    }

    private void updateBackground() {
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(buttonColor);
        backgroundDrawable.setCornerRadius(cornerRadius);
        backgroundDrawable.setStroke(borderWidth, borderColor);
        setBackground(backgroundDrawable);
    }



}
