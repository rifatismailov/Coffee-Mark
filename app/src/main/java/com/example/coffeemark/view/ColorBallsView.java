package com.example.coffeemark.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ColorBallsView extends View {
    private List<Integer> ballColors = new ArrayList<>();
    private float radius = 30f; // радіус куль

    private Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ColorBallsView(Context context) {
        super(context);
        init();
    }

    public ColorBallsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorBallsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(0xFFD3D3D3); // колір окантовки
        strokePaint.setStrokeWidth(4f);    // товщина окантовки
    }

    // Метод для встановлення кольорів куль
    public void setBallColors(List<Integer> colors) {
        this.ballColors = colors;
        invalidate(); // перемалювати View
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = 0;
        int ballCount = ballColors.size();
        int ballSize = (int)(radius * 2);
        int ballMargin = 20;

        desiredWidth = (ballSize + ballMargin) * ballCount;

        int width = resolveSize(desiredWidth, widthMeasureSpec);
        int height = resolveSize(ballSize, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float spacing = radius * 2 + 20; // Відстань між кулями
        float startX = radius + 10;

        for (int i = 0; i < ballColors.size(); i++) {
            float cx = startX + i * spacing;
            float cy = getHeight() / 2f;

            // Окантовка
            canvas.drawCircle(cx, cy, radius, strokePaint);

            // Кулька
            fillPaint.setColor(ballColors.get(i));
            fillPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cy, radius - 2f, fillPaint);
        }
    }
}
