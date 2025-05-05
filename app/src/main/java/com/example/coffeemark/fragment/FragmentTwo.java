package com.example.coffeemark.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeemark.R;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.cafe.CafeFound;
import com.example.coffeemark.cart_db.CartService;
import com.example.coffeemark.util.image.ImageHandler;

import java.util.ArrayList;
import java.util.List;

public class FragmentTwo extends Fragment implements CafeAdapter.OnItemClickListener {
    private final CartService cartService;


    /**
     * Список кавʼярень, які додає користувач.
     */
    private final List<Cafe> cafeList = new ArrayList<>();

    /**
     * Адаптер для відображення списку кавʼярень.
     */
    private CafeAdapter adapter;

    private final ImageHandler imageHandler;
    private final Context context;
    private OnCartListener onCartListener;

    public FragmentTwo(Context context) {
        this.context = context;
        imageHandler = new ImageHandler(context);
        cartService = new CartService(context);
        this.onCartListener = (OnCartListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cafeSearchCards);
        adapter = new CafeAdapter(cafeList, imageHandler, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showSearch(List<Cafe> cafeList) {
        this.cafeList.clear();
        this.cafeList.addAll(cafeList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(Cafe model) {
        new Thread(() -> {
            CafeCart cafeCart = cartService.isReturnInDatabase(model);
            if (cafeCart != null) {
                onCartListener.onItemClick(cafeCart);
            } else {
                cartService.setCart( new CafeFound.Builder()
                        .setName(model.getName())
                        .setAddress(model.getAddress())
                        .setCafeImage(model.getCafeImage())
                        .setAmountOfCoffee(0)
                        .build());
            }
        }).start();
    }


    public interface OnCartListener {
        void onItemClick(CafeCart cart);
    }
}

