package com.example.coffeemark.dialog;

import android.content.Context;

import com.example.coffeemark.R;
import com.example.coffeemark.view.CoffeeView;

public class StatusHandler {
    public static void handleStatus(String status, CoffeeView coffeeView) {
        switch (status) {
            case "0000":
                coffeeView.setImageResource(R.drawable.emoticon_cry);
                break;
            case "1001":
                coffeeView.setImageResource(R.drawable.emoticon_cry);
                break;
            case "1002":
                coffeeView.setImageResource(R.drawable.emoticon_angry);
                break;
            case "1003":
                coffeeView.setImageResource(R.drawable.emoticon_emoticon);
                break;
            case "1004":
                coffeeView.setImageResource(R.drawable.emoticon_face_suspicious);
                break;
            case "1005":
                coffeeView.setImageResource(R.drawable.emoticon_shocked);
                break;
            case "1006":
                coffeeView.setImageResource(R.drawable.emoticon_shocked_wonder);
                break;
            case "1007":
                coffeeView.setImageResource(R.drawable.emoticon_expression_face);
                break;
            case "1008":
                coffeeView.setImageResource(R.drawable.emoticon_expression_face);
                break;
            case "1009":
                coffeeView.setImageResource(R.drawable.emoticon_expression_face);
                break;
            case "1010":
                coffeeView.setImageResource(R.drawable.emoticon_forced_pity_silent_surrender);
                break;
            case "1011":
                coffeeView.setImageResource(R.drawable.emoticon_shocked_wonder);
                break;
            case "1012":
                coffeeView.setImageResource(R.drawable.emoticon_shocked_wonder);
                break;
            case "1013":
                coffeeView.setImageResource(R.drawable.emoticon_shocked_wonder);
                break;
            default:
                // Дії за замовчуванням, якщо потрібні
                break;
        }
    }
}


