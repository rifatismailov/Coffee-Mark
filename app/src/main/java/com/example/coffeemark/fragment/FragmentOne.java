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

import com.example.coffeemark.R;
import com.example.coffeemark.account.AccountManager;
import com.example.coffeemark.util.QRCode;
import com.example.coffeemark.cafe.CafeAdapter;
import com.example.coffeemark.cafe.CafeBase;
import com.example.coffeemark.cafe.CafeCart;
import com.example.coffeemark.util.CafeData;
import com.example.coffeemark.util.Decryptor;
import com.example.coffeemark.util.image.ImageHandler;
import com.example.coffeemark.view.CustomImageView;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FragmentOne extends Fragment implements CafeAdapter.OnItemClickListener {
    Context context;

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
    private CustomImageView customImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);
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

        for (int i = 0; i < 10; i++) {
            cafeList.add(new CafeCart("name cafe " + i, "address cafe " + i, "coffee_mark.png", 4));
            adapter.notifyItemInserted(cafeList.size() - 1); // Оновлюємо RecyclerView
        }


        return view;
    }


    @Override
    public void onItemClick(CafeBase model) {

        new CountDownTimer(15000, 1000) { // 15000 мс = 15 секунд, оновлюємо кожну 1 секунду

            public void onTick(long millisUntilFinished) {
                try {
                    PrivateKey privateKey = loadPrivateKey(context, "user_private.pem");
                    String qrCode = QRCode.generateQRCodeData(context, model.getName(), model.getAddress(), CafeData.getTime(), privateKey);
                    Bitmap bitmap = imageHandler.getBitmap(imageHandler.getDirFile(AccountManager.getImage(context)));
                    customImageView.setImageBitmap(QRCode.getQRCode(qrCode, bitmap)); // Генерація та встановлення QR-коду
                } catch (Exception e) {
                    Log.e("FragmentOne", "Error onItemClick: " + e);
                }
            }

            public void onFinish() {
                customImageView.setImageBitmap(null);
            }

        }.start();
    }

}
