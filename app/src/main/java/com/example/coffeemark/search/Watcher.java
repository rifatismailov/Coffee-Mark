package com.example.coffeemark.search;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;

import java.util.regex.Matcher;

public class Watcher implements TextWatcher {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private static final long DELAY_MS = 100; // затримка 500мс
    public OnWatcher onWatcher;

    public Watcher(OnWatcher onWatcher) {
        this.onWatcher = onWatcher;
    }

    //до изменении текста
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * при изменении текста и добавлениии текста и переходе на новую строку
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        handler.removeCallbacks(searchRunnable);
    }

    // после изменении текста
    @Override
    public void afterTextChanged(Editable s) {

        final String text = s.toString().trim();

        searchRunnable = () -> {
            if (text.isEmpty() || onWatcher.isEdit().getSelectionStart() == 0) {
                onWatcher.isEditEmpty();
            } else {
                if (!text.startsWith(" ")) {
                    onWatcher.isEditFull(text);
                }
            }
        };

        handler.postDelayed(searchRunnable, DELAY_MS);
        removeSpans(s, ForegroundColorSpan.class);
        for (Visualization.TextColor tetxtColor : Visualization.getColors()) {
            for (Matcher m = tetxtColor.pattern.matcher(s); m.find(); ) {
                s.setSpan(new ForegroundColorSpan(tetxtColor.color),
                        m.start(),
                        m.end(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
        CharacterStyle[] spans = e.getSpans(0, e.length(), type);
        for (CharacterStyle span : spans) {
            e.removeSpan(span);
        }
    }

    public interface OnWatcher {
        void isEditEmpty();

        void isEditFull(String text);

        EditText isEdit();
    }

}
