package com.example.coffeemark.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CafeData {
    public static String getTime() {
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Формат дати і часу
        return formatter.format(currentDate);
    }
}
