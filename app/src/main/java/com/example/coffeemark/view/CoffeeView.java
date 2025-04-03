package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CoffeeView extends AppCompatImageView {
    private Paint borderPaint;
    private Paint backgroundPaint;
    private RectF rect;
    private final float borderWidth = 4f;
    private int backgroundColor = 0xFF83898E;

    public CoffeeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Обвідна лінія
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        int borderColor = 0xFFD3D3D3;
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);

        // Фон
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rect == null) {
            rect = new RectF(borderWidth / 2, borderWidth / 2, getWidth() - borderWidth / 2, getHeight() - borderWidth / 2);
        }

        // Малюємо фон
        canvas.drawOval(rect, backgroundPaint);

        // Малюємо зображення
        super.onDraw(canvas);

        // Малюємо обвідну лінію
        canvas.drawOval(rect, borderPaint);
    }

    // Змінюємо колір фону
    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }
}
