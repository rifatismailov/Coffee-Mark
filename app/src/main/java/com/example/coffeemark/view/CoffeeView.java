package com.example.coffeemark.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class CoffeeView extends AppCompatImageView {
    private Paint borderPaint;
    private Paint backgroundPaint;
    private Paint progressPaint;
    private RectF rect;
    private final float borderWidth = 4f;
    private int backgroundColor = 0xFF83898E;
    private int progress = 0; // Значення прогресу від 0 до 100

    public CoffeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);

        // Ініціалізація обвідної лінії
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xFFD3D3D3);
        borderPaint.setStrokeWidth(borderWidth);

        // Ініціалізація фону
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        // Ініціалізація прогресу
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(0xFF228AB0); // Колір прогресу (зелений)
        progressPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(getWidth(), getHeight()) / 2f;
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        // Малюємо круглий фон
        canvas.drawCircle(cx, cy, radius - borderWidth / 2, backgroundPaint);

        // Створюємо круглу маску
        Path path = new Path();
        path.addCircle(cx, cy, radius - borderWidth / 2, Path.Direction.CCW);
        canvas.save();
        canvas.clipPath(path); // Обрізаємо все поза кругом

        // Малюємо зображення
        super.onDraw(canvas);
        canvas.restore();

        // Малюємо обвідну лінію
        canvas.drawCircle(cx, cy, radius - borderWidth / 2, borderPaint);

        // Малюємо прогрес, якщо він між 1 і 99
        if (progress > 0 && progress < 100) {
            @SuppressLint("DrawAllocation") RectF rectF = new RectF(
                    cx - radius + borderWidth / 2,
                    cy - radius + borderWidth / 2,
                    cx + radius - borderWidth / 2,
                    cy + radius - borderWidth / 2
            );

            float sweepAngle = (progress / 100f) * 360f; // Обчислення кута прогресу
            canvas.drawArc(rectF, -90, sweepAngle, false, progressPaint); // Малюємо прогрес по кругу
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }

    // Метод для оновлення прогресу
    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        this.progress = progress;
        invalidate(); // Оновлює вигляд
    }
}
