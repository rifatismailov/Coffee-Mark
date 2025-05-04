package com.example.coffeemark.fragment;


import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;
import static com.example.coffeemark.util.KeyUntil.loadPublicKey;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.coffeemark.R;
import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.cart_db.Cart;
import com.example.coffeemark.cart_db.CartService;
import com.example.coffeemark.util.QRCode;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.util.CafeData;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CustomImageView;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment implements CafeAdapter.OnItemClickListener, Cart {
    private Context context;
    private Cafe model;
    private CountDownTimer currentTimer;  // створюємо змінну класу

    public FragmentOne() {
    }

    /**
     * Список кавʼярень, які додає користувач.
     */
    private final List<Cafe> cafeList = new ArrayList<>();

    /**
     * Адаптер для відображення списку кавʼярень.
     */
    private CafeAdapter adapter;

    private ImageHandler imageHandler;
    private CustomImageView customImageView;
    private ScrollView scrollView;
    private CartService cartService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        scrollView = view.findViewById(R.id.scrollView2);
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
            this.context = context;
            imageHandler = new ImageHandler(context);
            cartService=new CartService(context);
        }

        PublicKey publicKey = loadPublicKey(context, "user_public.pem");

        customImageView = view.findViewById(R.id.showQr);

        adapter = new CafeAdapter(cafeList, imageHandler, this);
        viewPager.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            // Налаштування масштабу елементів
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f); // Зменшуємо картки позаду
        });
        viewPager.setPageTransformer(transformer);
        cartService.getCarts(this);
//        for (int i = 0; i < 10; i++) {
//            //new CafeCart("Kava Love" + i, "м. Одеса вул. Кавова, 7" + i, "coffee_mark.png", AccountManager.getImage(context),6)
//
//        }


        return view;
    }


    @Override
    public void onItemClick(Cafe model) {
        startTimer(model);
    }

    public void startTimer(Cafe model) {
        // Якщо попередній таймер працює — зупиняємо його
        if (currentTimer != null) {
            currentTimer.cancel();
        }

        // Створюємо новий таймер
        currentTimer = new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (scrollView.getVisibility() != View.VISIBLE) {
                    fadeIn(scrollView);
                }

                try {
                    PrivateKey privateKey = loadPrivateKey(context, "user_private.pem");
                    String qrCode = QRCode.generateQRCodeData(context, model.getName(), model.getAddress(), CafeData.getTime(), privateKey);
                    Bitmap bitmap = imageHandler.getBitmap(imageHandler.getDirFile(AccountManager.getImage(context)));
                    customImageView.setImageBitmap(QRCode.getQRCode(qrCode, bitmap));
                } catch (Exception e) {
                    Log.e("FragmentOne", "Error onItemClick: " + e);
                }
            }

            public void onFinish() {
                fadeOut(scrollView);
                customImageView.setImageBitmap(null);
            }


        }.start();
    }

    private void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(300).start();
    }

    private void fadeOut(View view) {
        view.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> view.setVisibility(View.GONE))
                .start();
    }

    @Override
    public void cart(CafeCart cart) {
        requireActivity().runOnUiThread(() -> {
            cafeList.add(new CafeCart.Builder()
                    .setName(cart.getName())
                    .setAddress(cart.getAddress())
                    .setCafeImage(cart.getCafeImage())
                    .setUser_image(AccountManager.getImage(context))
                    .setAmountOfCoffee(cart.getAmount_of_coffee())
                    .build());
            adapter.notifyItemInserted(cafeList.size() - 1);
        });
    }
}


