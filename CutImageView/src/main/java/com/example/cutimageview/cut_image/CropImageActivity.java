package com.example.cutimageview.cut_image;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class CropImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button cropButton;
    private Bitmap originalBitmap;
    private Rect cropRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout layout = new FrameLayout(this);
        imageView = new ImageView(this);
        cropButton = new Button(this);
        cropButton.setText("Обрізати");

        layout.addView(imageView);
        layout.addView(cropButton);

        cropButton.setOnClickListener(v -> {
            if (originalBitmap != null && cropRect != null) {
                Bitmap cropped = Bitmap.createBitmap(originalBitmap,
                        cropRect.left, cropRect.top, cropRect.width(), cropRect.height());
                imageView.setImageBitmap(cropped);
            }
        });

        setContentView(layout);

        // Отримуємо зображення з наміру
        Uri imageUri = getIntent().getData();
        if (imageUri != null) {
            try {
                originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(originalBitmap);

                int size = Math.min(originalBitmap.getWidth(), originalBitmap.getHeight());
                int left = (originalBitmap.getWidth() - size) / 2;
                int top = (originalBitmap.getHeight() - size) / 2;
                cropRect = new Rect(left, top, left + size, top + size);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

