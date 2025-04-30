package com.example.coffeemark.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

// Кастомний клас для NumberPicker
public  class CustomNumberPicker extends NumberPicker {
    public CustomNumberPicker(Context context) {
        super(context);
        applyCustomStyle();
        Log.e("CustomNumberPicker", "Paint CustomStyle -ЮЮ ");

    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomStyle();
    }

    private void applyCustomStyle() {
        try {
            Field selectorWheelPaintField = NumberPicker.class.getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            Paint paint = (Paint) selectorWheelPaintField.get(this);
            paint.setColor(Color.parseColor("#BB86FC")); // Колір тексту
            paint.setTypeface(Typeface.MONOSPACE); // Шрифт
            paint.setTextSize(48); // Розмір тексту

            // Оновлення NumberPicker
            invalidate();
            Log.e("CustomNumberPicker", "Paint CustomStyle: " + paint);

            // Зміна стилю текстових елементів
            Field[] fields = NumberPicker.class.getDeclaredFields();
            for (Field field : fields) {
                Log.e("CustomNumberPicker", "Paint object: " + paint);

                if (field.getName().equals("mInputText")) {
                    Log.e("CustomNumberPicker", "Paint object: " + paint);

                    field.setAccessible(true);
                    EditText editText = (EditText) field.get(this);
                    editText.setTextColor(Color.parseColor("#BB86FC"));
                    editText.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
                    editText.setTextSize(18);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}