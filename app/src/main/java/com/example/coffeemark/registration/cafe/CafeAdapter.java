package com.example.coffeemark.registration.cafe;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CoffeeView;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> {
    private List<Cafe> items; // Список елементів
    public ImageHandler imageHandler;

    public CafeAdapter(List<Cafe> items, ImageHandler imageHandler) {
        this.items = items;
        this.imageHandler = imageHandler;
    }

    // Створюємо ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafe_item, parent, false);
        return new ViewHolder(view);
    }

    // Прив’язуємо дані до ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cafe cafe = items.get(position);
        holder.cafeName.setText(cafe.getName());
        holder.cafeAddress.setText(cafe.getAddress());
        try {
            if (!cafe.getCafe_image().isEmpty()) {
                File imageFile = new File(imageHandler.getSaveDir(), cafe.getCafe_image());
                Uri uri = Uri.fromFile(imageFile); // Правильне створення Uri для локального файла
                holder.cafe_image.setImageBitmap(imageHandler.getBitmap(uri));
            }
        } catch (IOException e) {
            Log.e("CafeAdapter", e.toString());

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Вкладений клас ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cafeName;
        TextView cafeAddress;
        CoffeeView cafe_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cafeName = itemView.findViewById(R.id.cafe_real_name);
            cafeAddress = itemView.findViewById(R.id.cafe_real_address);
            cafe_image = itemView.findViewById(R.id.cafe_image);
        }
    }

    // Метод для додавання елементу в список
    public void addItem(Cafe cafe) {
        items.add(cafe);
        Log.e("RegisterActivity", cafe.getName() + " " + cafe.getAddress());
        notifyItemInserted(items.size() - 1); // Оновлюємо RecyclerView
    }
}
