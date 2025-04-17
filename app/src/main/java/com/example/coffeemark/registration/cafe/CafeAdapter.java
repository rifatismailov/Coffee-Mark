package com.example.coffeemark.registration.cafe;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.cafe.holder.CafeCartViewHolder;
import com.example.coffeemark.registration.cafe.holder.CafeShopViewHolder;
import com.example.coffeemark.util.image.ImageHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CafeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_CART = 1;
    private static final int TYPE_SHOP = 2;

    private List<CafeBase> items;
    public ImageHandler imageHandler;

    public CafeAdapter(List<CafeBase> items, ImageHandler imageHandler) {
        this.items = items;
        this.imageHandler = imageHandler;
    }

    @Override
    public int getItemViewType(int position) {
        CafeBase item = items.get(position);
        if (item instanceof CafeCart) return TYPE_CART;
        else if (item instanceof CafeShop) return TYPE_SHOP;
        else throw new IllegalStateException("Unknown CafeBase type at position: " + position);
    }

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CafeBase item = items.get(position);

        if (holder instanceof CafeCartViewHolder && item instanceof CafeCart) {
            CafeCart cart = (CafeCart) item;
            CafeCartViewHolder cartHolder = (CafeCartViewHolder) holder;
            cartHolder.cafeName.setText(cart.getName());
            cartHolder.cafeAddress.setText(cart.getAddress());

            List<Integer> colors = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                if (i < cart.getAmount_of_coffee()) colors.add(0xFF8F6E4F);
                else colors.add(0xFFFFFFFF);
            }
            cartHolder.colorBallsView.setBallColors(colors);

            try {
                if (!cart.getCafeImage().isEmpty()) {
                    cartHolder.cafe_image.setImageBitmap(
                            imageHandler.getBitmap(imageHandler.getDirFile(cart.getCafeImage()))
                    );
                }
            } catch (IOException e) {
                Log.e("CafeAdapter", e.toString());
            }

        } else if (holder instanceof CafeShopViewHolder && item instanceof CafeShop) {
            CafeShop shop = (CafeShop) item;
            CafeShopViewHolder shopHolder = (CafeShopViewHolder) holder;
            shopHolder.cafeName.setText(shop.getName());
            shopHolder.cafeAddress.setText(shop.getAddress());

            try {
                if (!shop.getCafeImage().isEmpty()) {
                    shopHolder.cafe_image.setImageBitmap(
                            imageHandler.getBitmap(imageHandler.getDirFile(shop.getCafeImage()))
                    );
                }
            } catch (IOException e) {
                Log.e("CafeAdapter", e.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(CafeBase cafe) {
        items.add(cafe);
        notifyItemInserted(items.size() - 1);
    }
}
