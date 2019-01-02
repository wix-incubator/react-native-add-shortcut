package com.reactnativeaddshortcut;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddShortcutModule extends ReactContextBaseJavaModule {

    private static final String NAME = "name";
    private static final String IMAGE_URL = "imageUrl";
    private static final String DEEP_LINK = "deepLink";

    public AddShortcutModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "AddShortcut";
    }

    private Bitmap getResizedBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int newWidth = 196;
        int newHeight = 196;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    private Bitmap getThumbnail(Bitmap bitmap) {
        final int THUMBSIZE = 196;

        return ThumbnailUtils.extractThumbnail(bitmap, THUMBSIZE, THUMBSIZE);
    }

    private Bitmap drawableFromUrl(String url) throws IOException {
        Bitmap bitmap;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        bitmap = BitmapFactory.decodeStream(input);

        return getThumbnail(bitmap);
    }

    @ReactMethod
    @TargetApi(25)
    private void setDynamicShortcuts(ReadableArray shortcuts) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            List<ShortcutInfo> list = new ArrayList<>();
            ShortcutManager mShortcutManager = getReactApplicationContext().getSystemService(ShortcutManager.class);
            for (int i = 0; i < shortcuts.size(); i++){
                String name = shortcuts.getMap(i).getString(NAME);
                String imageUrl = shortcuts.getMap(i).getString(IMAGE_URL);
                String deepLink = shortcuts.getMap(i).getString(DEEP_LINK);

                Bitmap bitmap = drawableFromUrl(imageUrl);

                Intent shortcutIntent = new Intent(getReactApplicationContext(), AddShortcutModule.class);
                shortcutIntent.setAction(Intent.ACTION_MAIN);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deepLink));

                ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(getReactApplicationContext(), name)
                        .setShortLabel(name)
                        .setLongLabel(name)
                        .setIntent(intent)
                        .setIcon(Icon.createWithBitmap(bitmap))
                        .build();
                list.add(shortcutInfo);
            }
            if (mShortcutManager != null) {
                mShortcutManager.setDynamicShortcuts(list);
            }
        }
    }

    @ReactMethod
    @TargetApi(25)
    private void setPinnedShortcuts(ReadableMap shortcut) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutManager mShortcutManager = getReactApplicationContext().getSystemService(ShortcutManager.class);
            String name = shortcut.getString(NAME);
            String imageUrl = shortcut.getString(IMAGE_URL);
            String deepLink = shortcut.getString(DEEP_LINK);

            Bitmap bitmap = drawableFromUrl(imageUrl);

            Intent shortcutIntent = new Intent(getReactApplicationContext(), AddShortcutModule.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(deepLink));

            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(getReactApplicationContext(), name)
                    .setShortLabel(name)
                    .setLongLabel(name)
                    .setIntent(intent)
                    .setIcon(Icon.createWithBitmap(bitmap))
                    .build();
            if (mShortcutManager != null) {
                mShortcutManager.requestPinShortcut(shortcutInfo, null);
            }
        }
    }

    @ReactMethod
    @TargetApi(25)
    private List<ShortcutInfo> getDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutManager mShortcutManager = getReactApplicationContext().getSystemService(ShortcutManager.class);
            return mShortcutManager != null ? mShortcutManager.getDynamicShortcuts() : null;
        }
        return null;
    }

    @ReactMethod
    @TargetApi(25)
    private void removeAllDynamicShortcuts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ShortcutManager mShortcutManager = getReactApplicationContext().getSystemService(ShortcutManager.class);
            if (mShortcutManager != null) {
                mShortcutManager.removeAllDynamicShortcuts();
            }
        }
    }
}
