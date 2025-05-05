package com.example.coffeemark.fragment;


import static com.example.coffeemark.util.KeyUntil.loadPrivateKey;

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
import com.example.coffeemark.cart_db.CartService;
import com.example.coffeemark.util.QRCode;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.cafe.Cafe;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.util.CafeData;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CustomImageView;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент, який відповідає за відображення списку кав'ярень у вигляді карток (ViewPager2),
 * генерацію QR-коду для вибраної кавʼярні та взаємодію з локальною базою даних (CartService).
 */
public class FragmentOne extends Fragment implements CafeAdapter.OnItemClickListener, CartService.Service.Cart {

    /** Поточний контекст фрагмента */
    private Context context;

    /** Поточна активна картка, передана з пошуку */
    private CafeCart cart;

    /** Таймер для показу QR-коду */
    private CountDownTimer currentTimer;

    /** ViewPager2 для вертикального скролу карток */
    private ViewPager2 viewPager;

    /** Список кавʼярень, які відображаються */
    private final List<Cafe> cafeList = new ArrayList<>();

    /** Адаптер для ViewPager2 */
    private CafeAdapter adapter;

    /** Обробник зображень для QR-коду */
    private ImageHandler imageHandler;

    /** Кастомне ImageView для показу QR-коду */
    private CustomImageView customImageView;

    /** ScrollView з додатковою інформацією */
    private ScrollView scrollView;

    /** Сервіс роботи з локальною базою даних */
    private CartService cartService;

    /** Приватний ключ для шифрування QR-коду */
    private PrivateKey privateKey;

    /** Конструктор без параметрів (обов’язковий для Fragment) */
    public FragmentOne() {}

    /** Конструктор з передачею картки для попереднього завантаження */
    public FragmentOne(CafeCart cart) {
        this.cart = cart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        scrollView = view.findViewById(R.id.scrollView2);
        viewPager = view.findViewById(R.id.viewPager);

        setupViewPager();

        Context context = getContext();
        if (context != null) {
            this.context = context;
            privateKey = loadPrivateKey(context, "user_private.pem");
            imageHandler = new ImageHandler(context);
            cartService = new CartService(context);
            customImageView = view.findViewById(R.id.showQr);
        }

        adapter = new CafeAdapter(cafeList, imageHandler, this);
        viewPager.setAdapter(adapter);

        cartService.getCarts(this);

        if (cart != null) {
            setCart(cart);
        }

        return view;
    }

    /**
     * Налаштовує ViewPager2 з трансформерами і властивостями.
     */
    private void setupViewPager() {
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setPadding(0, 0, 0, 100);
        viewPager.setOffscreenPageLimit(3);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager.setPageTransformer(transformer);
    }

    /**
     * Додає картку у список і запускає таймер з показом QR.
     */
    public void setCart(CafeCart cart) {
        cafeList.add(cart);
        adapter.notifyItemInserted(cafeList.size() - 1);
        startTimer(cart);
    }

    @Override
    public void onItemClick(Cafe model) {
        startTimer(model);
    }

    /**
     * Запускає таймер з оновленням QR-коду щосекунди.
     * @param model Кавʼярня для якої генерується QR-код
     */
    public void startTimer(Cafe model) {
        if (currentTimer != null) {
            currentTimer.cancel();
        }

        currentTimer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (scrollView.getVisibility() != View.VISIBLE) {
                    fadeIn(scrollView);
                }

                try {
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

    /**
     * Метод, що викликається після завантаження даних з бази (реалізація Cart).
     * @param newCart Картка, яку потрібно додати у список, якщо її ще немає
     */

    @Override
    public void showCart(CafeCart newCart) {
        requireActivity().runOnUiThread(() -> {
            if (!newCart.equals(cart)) {
                cafeList.add(new CafeCart.Builder()
                        .setName(newCart.getName())
                        .setAddress(newCart.getAddress())
                        .setCafeImage(newCart.getCafeImage())
                        .setUser_image(AccountManager.getImage(context))
                        .setAmountOfCoffee(newCart.getAmount_of_coffee())
                        .build());
                adapter.notifyItemInserted(cafeList.size() - 1);
            }
        });
    }
}



