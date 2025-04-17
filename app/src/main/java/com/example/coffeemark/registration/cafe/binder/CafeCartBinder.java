package com.example.coffeemark.registration.cafe.binder;

import android.util.Log;

import com.example.coffeemark.registration.cafe.CafeCart;
import com.example.coffeemark.registration.cafe.holder.CafeCartViewHolder;
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

        try {
            // Якщо зображення вказано — намагаємося завантажити його та встановити
            if (!cart.getCafeImage().isEmpty()) {
                holder.cafe_image.setImageBitmap(
                        imageHandler.getBitmap(imageHandler.getDirFile(cart.getCafeImage()))
                );
            }
        } catch (IOException e) {
            // У разі помилки при завантаженні — логгування
            Log.e("CafeCartBinder", e.toString());
        }
    }
}
