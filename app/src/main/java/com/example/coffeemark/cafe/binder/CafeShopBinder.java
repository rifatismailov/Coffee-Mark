package com.example.coffeemark.cafe.binder;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.coffeemark.R;
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


        // Якщо зображення вказано — намагаємося завантажити його та встановити
        // Використовуємо Glide для завантаження зображення
        if (!shop.getCafeImage().isEmpty()) {
            Glide.with(holder.cafe_image.getContext())
                    .load(imageHandler.getDirFile(shop.getCafeImage()))
                    .placeholder(R.drawable.emoticon_shame_smiley) // Можеш задати тимчасову картинку на час завантаження
                    .error(R.drawable.emoticon_cry)              // Картинка при помилці
                    .into(holder.cafe_image);
        } else {
            holder.cafe_image.setImageResource(R.drawable.emoticon_shame_smiley);
        }
    }
}
