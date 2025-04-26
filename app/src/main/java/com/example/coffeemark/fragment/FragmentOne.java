package com.example.coffeemark.fragment;


import static com.example.coffeemark.service.Manager.checkPublicKey;
import static com.example.coffeemark.util.KeyUntil.checkLocalKey;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coffeemark.R;
import com.example.coffeemark.registration.cafe.CafeAdapter;
import com.example.coffeemark.registration.cafe.CafeBase;
import com.example.coffeemark.registration.cafe.CafeCart;
import com.example.coffeemark.util.image.ImageHandler;

import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment {

    public FragmentOne() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_one, container, false);
                //RecyclerView recyclerView = findViewById(R.id.mainCafeList);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setPadding(0, 0, 0, 100); // Збільшення відступу знизу
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        Context context = getContext();
        if (context != null) {
            imageHandler = new ImageHandler(context);
        }

        adapter = new CafeAdapter(cafeList, imageHandler);
        viewPager.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            // Налаштування масштабу елементів
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Зменшуємо картки позаду
        });
        viewPager.setPageTransformer(transformer);

        for (int i = 0; i < 10; i++) {
            cafeList.add(new CafeCart("name cafe "+i, "address cafe "+i, "coffee_mark.png",4));
            adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView
        }
        return view;
    }
}
