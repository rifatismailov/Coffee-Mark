package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.coffeemark.R;

public class CustomImageView extends AppCompatImageView {

    private int cornerRadius;  // Радіус кутів
    private int backgroundColor; // Колір фону
    private int borderColor;  // Колір рамки
    private int borderWidth;  // Ширина рамки

    private Bitmap imageBitmap;  // Зображення

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Малюємо фон і рамку
        if (borderWidth > 0) {
            Paint borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            RectF borderRect = new RectF(borderWidth / 2f, borderWidth / 2f, getWidth() - borderWidth / 2f, getHeight() - borderWidth / 2f);
            canvas.drawRoundRect(borderRect, cornerRadius, cornerRadius, borderPaint);
        }

        // Малюємо зображення, якщо воно встановлене
        if (imageBitmap != null) {
            Paint imagePaint = new Paint();
            imagePaint.setAntiAlias(true);
            RectF imageRect = new RectF(borderWidth, borderWidth, getWidth() - borderWidth, getHeight() - borderWidth);
            canvas.drawBitmap(imageBitmap, null, imageRect, imagePaint);
        }
    }

    public void setImageWithBorder(Bitmap bm) {
        imageBitmap = bm;
        invalidate();  // Перемалювати ImageView з новим зображенням та рамкою
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        invalidate();  // Перемалювати після зміни радіусу
    }

    public void setBackgroundColorCustom(int color) {
        this.backgroundColor = color;
        invalidate();
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
        invalidate();
    }

    public void setBorderWidth(int width) {
        this.borderWidth = width;
        invalidate();
    }
}
