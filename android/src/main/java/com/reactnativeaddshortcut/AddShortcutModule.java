package com.reactnativeaddshortcut;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

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

    private Bitmap getCircularBitmap(Bitmap srcBitmap) {
        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

        Bitmap dstBitmap = Bitmap.createBitmap(
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        Canvas canvas = new Canvas(dstBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        float left = (squareBitmapWidth-srcBitmap.getWidth())/2;
        float top = (squareBitmapWidth-srcBitmap.getHeight())/2;
        canvas.drawBitmap(srcBitmap, left, top, paint);

        srcBitmap.recycle();

        return dstBitmap;
    }

    @Override
    public String getName() {
        return "AddShortcut";
    }

    private Bitmap drawableFromUrl(String url) throws IOException {
        Bitmap bitmap;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        bitmap = BitmapFactory.decodeStream(input);

        return getCircularBitmap(bitmap);
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
