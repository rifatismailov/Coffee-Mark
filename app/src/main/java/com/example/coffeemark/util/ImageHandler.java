package com.example.coffeemark.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ImageHandler {

    private final Context context;
    private final File saveDir;
    private String savedFileName;

    public ImageHandler(Context context) {
        this.context = context;
        this.saveDir = new File(context.getExternalFilesDir(null), "Coffee_Image");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    public File processAndSaveImage(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        String fileName = UUID.randomUUID().toString();
        savedFileName = new BitmapToFile().saveBitmapToFile(bitmap, fileName, saveDir);
        return new File(saveDir, savedFileName);
    }

    public String getSavedFileName() {
        return savedFileName;
    }

    public Bitmap getBitmap(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
    }

    public File getSaveDir() {
        return saveDir;
    }
}

