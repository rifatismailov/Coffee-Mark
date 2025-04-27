package com.example.coffeemark.cafe.binder;

import android.util.Log;

import com.example.coffeemark.cafe.CafeShop;
import com.example.coffeemark.cafe.holder.CafeShopViewHolder;
import com.example.coffeemark.util.image.ImageHandler;

import java.io.IOException;

/**
 * Клас {@code CafeShopBinder} відповідає за прив'язку даних {@link CafeShop}
 * до відповідного {@link CafeShopViewHolder} у RecyclerView.
 * Реалізує інтерфейс {@link CafeViewHolderBinder}, забезпечуючи універсальність.
 */
public class CafeShopBinder implements CafeViewHolderBinder<CafeShopViewHolder, CafeShop> {

    /**
     * Метод прив'язує дані про кафе-магазин до візуального відображення у ViewHolder'і.
     *
     * @param holder       ViewHolder, який містить UI-елементи для {@link CafeShop}
     * @param shop         Об'єкт {@link CafeShop}, який містить інформацію про кафе-магазин
     * @param imageHandler Об'єкт для завантаження локальних зображень
     */
    @Override
    public void bind(CafeShopViewHolder holder, CafeShop shop, ImageHandler imageHandler) {

        // Встановлюємо назву кафе
        holder.cafeName.setText(shop.getName());

        // Встановлюємо адресу кафе
        holder.cafeAddress.setText(shop.getAddress());

        try {
            // Якщо зображення вказано — намагаємося завантажити його та встановити
            if (!shop.getCafeImage().isEmpty()) {
                holder.cafe_image.setImageBitmap(
                        imageHandler.getBitmap(imageHandler.getDirFile(shop.getCafeImage()))
                );
            }
        } catch (IOException e) {
            // У разі помилки при завантаженні — логгування
            Log.e("CafeShopBinder", e.toString());
        }
    }
}
