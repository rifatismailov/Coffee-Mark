package com.example.coffeemark.view;

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
        setScaleType(ScaleType.CENTER_CROP);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xFFD3D3D3);
        borderPaint.setStrokeWidth(borderWidth);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);
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
        canvas.clipPath(path); // обрізаємо все поза кругом

        // Малюємо зображення
        super.onDraw(canvas);
        canvas.restore();

        // Малюємо обвідну лінію
        canvas.drawCircle(cx, cy, radius - borderWidth / 2, borderPaint);
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }
}
