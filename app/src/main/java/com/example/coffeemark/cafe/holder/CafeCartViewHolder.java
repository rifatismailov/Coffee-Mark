package com.example.coffeemark.cafe.holder;

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
    public final CoffeeView user_image;

    public final ColorBallsView colorBallsView;
    public final TextView amount_of_coffee;
    public CafeCartViewHolder(@NonNull View itemView) {
        super(itemView);
        cafeName = itemView.findViewById(R.id.cafe_real_name);
        cafeAddress = itemView.findViewById(R.id.cafe_real_address);
        cafe_image = itemView.findViewById(R.id.cafe_image);
        user_image = itemView.findViewById(R.id.user_image);
        colorBallsView = itemView.findViewById(R.id.colorBallsView);
        amount_of_coffee=itemView.findViewById(R.id.amount_of_coffee);
    }


}
