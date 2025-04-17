package com.example.coffeemark.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.coffeemark.R; // Замініть на ваш пакет

public class CoffeeView extends AppCompatImageView {
    private Paint borderPaint;
    private Paint backgroundPaint;
    private Paint outerBackgroundPaint; // Для заднього зеленого фону
    private Paint progressPaint;
    private final float borderWidth = 4f;
    private int backgroundColor = 0xFF83898E; // Колір середини (вихідний колір)
    private int outerBackgroundColor = 0xFF00FF00; // Зелений колір для заднього фону
    private int progress = 0; // Значення прогресу від 0 до 100

    public CoffeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setScaleType(ScaleType.CENTER_CROP);

        // Зчитування атрибутів із XML
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CoffeeView);
            outerBackgroundColor = a.getColor(R.styleable.CoffeeView_outerBackgroundColor, outerBackgroundColor);
            a.recycle();
        }

        // Ініціалізація обвідної лінії
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xFFD3D3D3);
        borderPaint.setStrokeWidth(borderWidth);

        // Ініціалізація середнього фону
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        // Ініціалізація заднього зеленого фону
        outerBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerBackgroundPaint.setStyle(Paint.Style.FILL);
        outerBackgroundPaint.setColor(outerBackgroundColor);

        // Ініціалізація прогресу
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setColor(0xFF228AB0); // Колір прогресу
        progressPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = Math.min(getWidth(), getHeight()) / 2f;
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        // Малюємо задній зелений фон, який повністю перекриває вигляд
        canvas.drawRect(0, 0, getWidth(), getHeight(), outerBackgroundPaint);

        // Малюємо круглий фон (середину кола)
        canvas.drawCircle(cx, cy, radius - borderWidth / 2, backgroundPaint);

        // Створюємо круглу маску
        canvas.save(); // Зберігаємо стан полотна
        Path circularPath = new Path();
        circularPath.addCircle(cx, cy, radius - borderWidth, Path.Direction.CCW); // Коло для обрізання
        canvas.clipPath(circularPath); // Застосовуємо круглу маску

        // Малюємо зображення (воно обрізатиметься по кругу)
        super.onDraw(canvas);

        // Відновлюємо стан полотна
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

    // Метод для зміни кольору заднього зеленого фону
    public void setOuterBackgroundColor(int color) {
        this.outerBackgroundColor = color;
        outerBackgroundPaint.setColor(color);
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
