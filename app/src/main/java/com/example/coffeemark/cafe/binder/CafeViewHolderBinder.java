package com.example.coffeemark.cafe.binder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.cafe.InCafeBase;
import com.example.coffeemark.util.image.ImageHandler;

public interface CafeViewHolderBinder<T extends RecyclerView.ViewHolder, V extends InCafeBase> {
    void bind(T holder, V item, ImageHandler imageHandler);
}

