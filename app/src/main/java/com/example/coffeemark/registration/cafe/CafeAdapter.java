package com.example.coffeemark.registration.cafe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;

import java.util.List;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.ViewHolder> {
    private List<Cafe> items; // Список елементів

    public CafeAdapter(List<Cafe> items) {
        this.items = items;
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Вкладений клас ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cafeName;
        TextView cafeAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cafeName = itemView.findViewById(R.id.cafe_real_name);
            cafeAddress = itemView.findViewById(R.id.cafe_real_address);
        }
    }

    // Метод для додавання елементу в список
    public void addItem(Cafe cafe) {
        items.add(cafe);
        Log.e("RegisterActivity",cafe.getName()+" "+cafe.getAddress());
        notifyItemInserted(items.size() - 1); // Оновлюємо RecyclerView
    }
}
