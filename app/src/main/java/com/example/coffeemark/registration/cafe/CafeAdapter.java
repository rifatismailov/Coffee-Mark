package com.example.coffeemark.registration.cafe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.cafe.binder.CafeCartBinder;
import com.example.coffeemark.registration.cafe.binder.CafeShopBinder;
import com.example.coffeemark.registration.cafe.holder.CafeCartViewHolder;
import com.example.coffeemark.registration.cafe.holder.CafeShopViewHolder;
import com.example.coffeemark.util.image.ImageHandler;

import java.util.List;

/**
 * Адаптер для RecyclerView, який відображає список об'єктів {@link CafeBase},
 * включаючи {@link CafeCart} і {@link CafeShop}.
 * Для кожного типу використовується свій ViewHolder та логіка прив'язки через відповідний Binder.
 */
public class CafeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Типи елементів, які відображаються в списку
    private static final int TYPE_CART = 1;
    private static final int TYPE_SHOP = 2;

    // Об'єкти Binder'ів для різних типів ViewHolder'ів
    private final CafeCartBinder cartBinder = new CafeCartBinder();
    private final CafeShopBinder shopBinder = new CafeShopBinder();

    // Список елементів для відображення
    private final List<CafeBase> items;

    // Обробник зображень, використовується для завантаження локальних фото
    public ImageHandler imageHandler;

    /**
     * Конструктор адаптера.
     *
     * @param items        список елементів {@link CafeBase}, які буде відображено
     * @param imageHandler об'єкт для завантаження зображень
     */
    public CafeAdapter(List<CafeBase> items, ImageHandler imageHandler) {
        this.items = items;
        this.imageHandler = imageHandler;
    }

    /**
     * Визначає тип View для конкретної позиції списку.
     *
     * @param position позиція елемента у списку
     * @return {@link #TYPE_CART} якщо це {@link CafeCart}, {@link #TYPE_SHOP} якщо {@link CafeShop}
     */
    @Override
    public int getItemViewType(int position) {
        CafeBase item = items.get(position);
        if (item instanceof CafeCart) return TYPE_CART;
        else if (item instanceof CafeShop) return TYPE_SHOP;
        else throw new IllegalStateException("Unknown CafeBase type at position: " + position);
    }

    /**
     * Створює відповідний ViewHolder залежно від типу елемента.
     *
     * @param parent   ViewGroup, до якого буде додано View
     * @param viewType тип елемента (отриманий через {@link #getItemViewType(int)})
     * @return новий ViewHolder для відповідного типу
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_CART) {
            View view = inflater.inflate(R.layout.cafe_item_ball, parent, false);
            return new CafeCartViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.cafe_item, parent, false);
            return new CafeShopViewHolder(view);
        }
    }

    /**
     * Прив'язує дані до відповідного ViewHolder'а, використовуючи відповідний Binder.
     *
     * @param holder   ViewHolder, до якого треба прив'язати дані
     * @param position позиція елемента у списку
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CafeBase item = items.get(position);

        if (holder instanceof CafeCartViewHolder && item instanceof CafeCart) {
            cartBinder.bind((CafeCartViewHolder) holder, (CafeCart) item, imageHandler);

        } else if (holder instanceof CafeShopViewHolder && item instanceof CafeShop) {
            shopBinder.bind((CafeShopViewHolder) holder, (CafeShop) item, imageHandler);
        }
    }

    /**
     * Повертає загальну кількість елементів у списку.
     *
     * @return кількість елементів
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Додає новий елемент у список і повідомляє RecyclerView про зміну.
     *
     * @param cafe новий елемент типу {@link CafeBase}
     */
    public void addItem(CafeBase cafe) {
        items.add(cafe);
        notifyItemInserted(items.size() - 1);
    }
}
