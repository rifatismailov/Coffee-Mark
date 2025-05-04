package com.example.coffeemark.cafe.binder;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.coffeemark.R;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.cafe.CafeFound;
import com.example.coffeemark.cafe.holder.CafeFoundViewHolder;
import com.example.coffeemark.util.image.ImageHandler;

/**
 * Клас {@code CafeCartBinder} відповідає за прив'язку даних {@link CafeCart}
 * до відповідного {@link CafeFoundViewHolder} у RecyclerView.
 * Реалізує інтерфейс {@link CafeViewHolderBinder}, забезпечуючи універсальність.
 */
public class CafeFoundBinder implements CafeViewHolderBinder<CafeFoundViewHolder, CafeFound> {

    /**
     * Метод прив'язує дані про кафе-картку до візуального відображення у ViewHolder'і.
     *
     * @param holder       ViewHolder, який містить UI-елементи для {@link CafeCart}
     * @param cart         Об'єкт {@link CafeCart}, який містить інформацію про кафе
     * @param imageHandler Об'єкт для завантаження локальних зображень
     */
    @Override
    public void bind(CafeFoundViewHolder holder, CafeFound cart, ImageHandler imageHandler) {
        // Встановлюємо назву кафе
        holder.cafeName.setText(cart.getName());

        // Встановлюємо адресу кафе
        holder.cafeAddress.setText(cart.getAddress());

        if (cart.isInDatabase()) holder.isInDatabase.setImageResource(R.drawable.ic_check_save);
        else holder.isInDatabase.setImageResource(R.drawable.ic_check_not_save);

        // Якщо зображення вказано — намагаємося завантажити його та встановити
        // Використовуємо Glide для завантаження зображення
        Log.e("CafeFoundBinder",imageHandler.getDirFile(cart.getCafeImage()).getPath());
        if (cart.getCafeImage() != null && !cart.getCafeImage().isEmpty()) {
            Glide.with(holder.cafe_image.getContext())
                    .load(imageHandler.getDirFile(cart.getCafeImage()))
                    .placeholder(R.drawable.emoticon_shame_smiley)
                    .error(R.drawable.emoticon_cry)
                    .into(holder.cafe_image);
        } else {
            holder.cafe_image.setImageResource(R.drawable.emoticon_shame_smiley);
        }

    }
}
