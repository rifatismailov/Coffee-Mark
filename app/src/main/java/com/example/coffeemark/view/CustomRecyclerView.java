package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;

public class CustomRecyclerView extends RecyclerView {

    private int borderColor;
    private int borderWidth;
    private int cornerRadius;
    private int backgroundColor;

    public CustomRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final android.content.res.TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.CustomRecyclerView, 0, 0);

            try {
                backgroundColor = a.getColor(R.styleable.CustomRecyclerView_backgroundColor, Color.WHITE);
                borderColor = a.getColor(R.styleable.CustomRecyclerView_borderColor, Color.GRAY);
                borderWidth = a.getDimensionPixelSize(R.styleable.CustomRecyclerView_borderWidth, 2);
                cornerRadius = a.getDimensionPixelSize(R.styleable.CustomRecyclerView_cornerRadius, 8);
            } finally {
                a.recycle();
            }
        }

        updateBackground();
    }

    private void updateBackground() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(backgroundColor);
        drawable.setCornerRadius(cornerRadius);
        drawable.setStroke(borderWidth, borderColor);
        setBackground(drawable);
    }

    // Публічні сеттери для динамічного оновлення
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        updateBackground();
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
        updateBackground();
    }

    public void setBackgroundColorCustom(int color) {
        this.backgroundColor = color;
        updateBackground();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        updateBackground();
    }
}
