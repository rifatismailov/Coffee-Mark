package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.coffeemark.R;

import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageView extends AppCompatImageView {

    private int cornerRadius;  // Радіус кутів
    private int backgroundColor; // Колір фону
    private int borderColor;  // Колір рамки
    private int borderWidth;  // Ширина рамки

    public CustomImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final android.content.res.TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.CustomImageView, 0, 0);

            try {
                backgroundColor = a.getColor(R.styleable.CustomImageView_backgroundColor, Color.TRANSPARENT);
                borderColor = a.getColor(R.styleable.CustomImageView_borderColor, Color.BLACK);
                cornerRadius = a.getDimensionPixelSize(R.styleable.CustomImageView_cornerRadius, 0);
                borderWidth = a.getDimensionPixelSize(R.styleable.CustomImageView_borderWidth, 0);
            } finally {
                a.recycle();
            }
        }

        updateBackground();
    }

    private void updateBackground() {
        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(backgroundColor);
        backgroundDrawable.setCornerRadius(cornerRadius);
        backgroundDrawable.setStroke(borderWidth, borderColor);
        setBackground(backgroundDrawable);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        updateBackground();
    }

    public void setBackgroundColorCustom(int color) {
        this.backgroundColor = color;
        updateBackground();
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
        updateBackground();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        updateBackground();
    }
}

