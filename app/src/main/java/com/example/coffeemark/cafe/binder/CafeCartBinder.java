package com.example.coffeemark.cafe.binder;

import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.coffeemark.R;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.cafe.holder.CafeCartViewHolder;
import com.example.coffeemark.util.image.ImageHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас {@code CafeCartBinder} відповідає за прив'язку даних {@link CafeCart}
 * до відповідного {@link CafeCartViewHolder} у RecyclerView.
 * Реалізує інтерфейс {@link CafeViewHolderBinder}, забезпечуючи універсальність.
 */
public class CafeCartBinder implements CafeViewHolderBinder<CafeCartViewHolder, CafeCart> {

    /**
     * Метод прив'язує дані про кафе-картку до візуального відображення у ViewHolder'і.
     *
     * @param holder       ViewHolder, який містить UI-елементи для {@link CafeCart}
     * @param cart         Об'єкт {@link CafeCart}, який містить інформацію про кафе
     * @param imageHandler Об'єкт для завантаження локальних зображень
     */
    @Override
    public void bind(CafeCartViewHolder holder, CafeCart cart, ImageHandler imageHandler) {

        // Встановлюємо назву кафе
        holder.cafeName.setText(cart.getName());

        // Встановлюємо адресу кафе
        holder.cafeAddress.setText(cart.getAddress());

        // Створюємо список кольорових кружечків (імовірно для відображення кількості кави)
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // Якщо кількість кави більша за індекс — використовуємо коричневий колір
            // Інакше — білий (порожній кружечок)
            colors.add(i < cart.getAmount_of_coffee() ? 0xFF8F6E4F : 0xFFFFFFFF);
        }

        // Передаємо кольори до кастомного View для відображення
        holder.colorBallsView.setBallColors(colors);


        // Якщо зображення вказано — намагаємося завантажити його та встановити
        // Використовуємо Glide для завантаження зображення
        if (!cart.getCafeImage().isEmpty()) {
            Glide.with(holder.cafe_image.getContext())
                    .load(imageHandler.getDirFile(cart.getCafeImage()))
                    .placeholder(R.drawable.emoticon_shame_smiley) // Можеш задати тимчасову картинку на час завантаження
                    .error(R.drawable.emoticon_cry)              // Картинка при помилці
                    .into(holder.cafe_image);
        } else {
            holder.cafe_image.setImageResource(R.drawable.emoticon_shame_smiley);
        }
    }
}
