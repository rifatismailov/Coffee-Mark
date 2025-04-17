package com.example.coffeemark.registration.cafe.binder;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.registration.cafe.CafeBase;
import com.example.coffeemark.util.image.ImageHandler;

public interface CafeViewHolderBinder<T extends RecyclerView.ViewHolder, V extends CafeBase> {
    void bind(T holder, V item, ImageHandler imageHandler);
}

