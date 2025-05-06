package com.example.coffeemark.option;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;

public class ScrollPickerAdapter extends RecyclerView.Adapter<ScrollPickerAdapter.ViewHolder> {

    private final String[] items;
    private int selectedPosition = 0;
    private final OnItemSelected listener;

    public interface OnItemSelected {
        void onItemSelected(String item);
    }

    public ScrollPickerAdapter(String[] items, OnItemSelected listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_option);
        }
    }

    @NonNull
    @Override
    public ScrollPickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        int selectedColor = ContextCompat.getColor(context, R.color.cube_blue);
        int defaultColor = ContextCompat.getColor(context, R.color.white);
        holder.text.setText(items[position]);
        holder.text.setTextColor(position == selectedPosition ?selectedColor :defaultColor);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    // Метод, який викликається з Activity після зупинки скролу
    public void selectPosition(int position) {
        if (position >= 0 && position < items.length && position != selectedPosition) {
            selectedPosition = position;
            listener.onItemSelected(items[position]);
            notifyDataSetChanged();
        }
    }
}
