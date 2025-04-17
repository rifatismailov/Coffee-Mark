package com.example.coffeemark.registration.cafe.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.view.CoffeeView;
import com.example.coffeemark.view.ColorBallsView;

// ViewHolder для CafeCart
public class CafeCartViewHolder extends RecyclerView.ViewHolder {
    public final TextView cafeName;
    public final TextView cafeAddress;
    public final CoffeeView cafe_image;
    public final ColorBallsView colorBallsView;

    public CafeCartViewHolder(@NonNull View itemView) {
        super(itemView);
        cafeName = itemView.findViewById(R.id.cafe_real_name);
        cafeAddress = itemView.findViewById(R.id.cafe_real_address);
        cafe_image = itemView.findViewById(R.id.cafe_image);
        colorBallsView = itemView.findViewById(R.id.colorBallsView);
    }


}
