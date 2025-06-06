package com.example.coffeemark.cafe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.cafe.binder.CafeCartBinder;
import com.example.coffeemark.cafe.binder.CafeShopBinder;
import com.example.coffeemark.cafe.binder.CafeFoundBinder;
import com.example.coffeemark.cafe.holder.CafeCartViewHolder;
import com.example.coffeemark.cafe.holder.CafeShopViewHolder;
import com.example.coffeemark.cafe.holder.CafeFoundViewHolder;
import com.example.coffeemark.util.image.ImageHandler;

import java.util.List;

/**
 * Адаптер для RecyclerView, який відображає список об'єктів {@link Cafe},
 * включаючи {@link CafeCart} і {@link CafeShop}.
 * Для кожного типу використовується свій ViewHolder та логіка прив'язки через відповідний Binder.
 */
public class CafeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Типи елементів, які відображаються в списку
    private static final int TYPE_CART = 1;
    private static final int TYPE_SHOP = 2;
    private static final int TYPE_F_CART = 3;

    // Об'єкти Binder'ів для різних типів ViewHolder'ів
    private final CafeCartBinder cartBinder = new CafeCartBinder();
    private final CafeShopBinder shopBinder = new CafeShopBinder();
    private final CafeFoundBinder fCartBinder = new CafeFoundBinder();
    // Список елементів для відображення
    private final List<Cafe> items;

    // Обробник зображень, використовується для завантаження локальних фото
    public ImageHandler imageHandler;

    private OnItemClickListener onItemClickListener;

    /**
     * Конструктор адаптера.
     *
     * @param items        список елементів {@link Cafe}, які буде відображено
     * @param imageHandler об'єкт для завантаження зображень
     */
    public CafeAdapter(List<Cafe> items, ImageHandler imageHandler, OnItemClickListener onItemClickListener) {
        this.items = items;
        this.imageHandler = imageHandler;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * Визначає тип View для конкретної позиції списку.
     *
     * @param position позиція елемента у списку
     * @return {@link #TYPE_CART} якщо це {@link CafeCart}, {@link #TYPE_SHOP} якщо {@link CafeShop}
     */
    @Override
    public int getItemViewType(int position) {
        Cafe item = items.get(position);
        if (item instanceof CafeCart) return TYPE_CART;
        else if (item instanceof CafeShop) return TYPE_SHOP;
        else if (item instanceof CafeFound) return TYPE_F_CART;
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
        } else if (viewType == TYPE_SHOP) {
            View view = inflater.inflate(R.layout.cafe_item, parent, false);
            return new CafeShopViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.cafe_item_found, parent, false);
            return new CafeFoundViewHolder(view);
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
        Cafe item = items.get(position);

        if (holder instanceof CafeCartViewHolder && item instanceof CafeCart) {
            cartBinder.bind((CafeCartViewHolder) holder, (CafeCart) item, imageHandler);
            holder.itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(item);
            });

        } else if (holder instanceof CafeShopViewHolder && item instanceof CafeShop) {
            shopBinder.bind((CafeShopViewHolder) holder, (CafeShop) item, imageHandler);

        } else if (holder instanceof CafeFoundViewHolder && item instanceof CafeFound) {
            fCartBinder.bind((CafeFoundViewHolder) holder, (CafeFound) item, imageHandler);
            holder.itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(item);
            });
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
     * @param cafe новий елемент типу {@link Cafe}
     */
    public void addItem(Cafe cafe) {
        items.add(cafe);
        notifyItemInserted(items.size() - 1);
    }

    public interface OnItemClickListener {
        void onItemClick(Cafe model);
    }

}
