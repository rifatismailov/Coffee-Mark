package com.example.coffeemark.cafe.binder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.util.image.ImageHandler;

public interface CafeViewHolderBinder<T extends RecyclerView.ViewHolder, V extends Cafe> {
    void bind(T holder, V item, ImageHandler imageHandler);
}

