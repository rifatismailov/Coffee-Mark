package com.example.coffeemark.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeemark.R;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.cafe.CafeBase;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.cafe.FCafeCart;
import com.example.coffeemark.util.image.ImageHandler;

import java.util.ArrayList;
import java.util.List;

public class FragmentTwo extends Fragment implements CafeAdapter.OnItemClickListener {

    public FragmentTwo() {
    }

    /**
     * Список кавʼярень, які додає користувач.
     */
    private final List<CafeBase> cafeList = new ArrayList<>();

    /**
     * Адаптер для відображення списку кавʼярень.
     */
    private CafeAdapter adapter;

    private ImageHandler imageHandler;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.cafeSearchCards);

        Context context = getContext();
        if (context != null) {
            this.context = context;
            imageHandler = new ImageHandler(context);
        }

        adapter = new CafeAdapter(cafeList, imageHandler, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < 10; i++) {
            cafeList.add(new FCafeCart("name cafe " + i, "address cafe " + i, "", 4));
            adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView
        }
        return view;
    }

    @Override
    public void onItemClick(CafeBase model) {

    }
}

