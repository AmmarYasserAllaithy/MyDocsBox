package com.example.ammaryasser.mydocsbox.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Utils {
    private final Context context;

    public Utils(Context context) {
        this.context = context;
    }


    public static String getCurrentFormattedDate() {
        return formatDate(new Date());
    }

    public static String formatDate(long ts) {
        return formatDate(new Date(ts));
    }

    public static String formatDate(Date date) {
        return formatDate(date, "E, dd-MMM-yyyy, HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int mode) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xffC9E9E9); //424242);

        float rPercentage = mode == 1 ? 3 : 10;
        float rx = w * rPercentage / 100;
        float ry = h * rPercentage / 100;
        float r = Math.min(rx, ry);
        if (mode == 1) rx = ry = r;

        canvas.drawRoundRect(rectF, rx, ry, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void copyAndToast(String label, String text) {
        copyToClipboard(label, text);
        toast("Copied!");
    }

    public void copyToClipboard(String label, String text) {
        copyToClipboard(context, label, text);
    }

    public static void copyToClipboard(Context context, String label, String text) {
        ((ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE))
                .setPrimaryClip(ClipData.newPlainText(label, text));
    }


    public void toast(String message) {
        toast(context, message, 0);
    }

    public void toast(String message, int length) {
        toast(context, message, length);
    }

    public static void toast(Context context, String message) {
        toast(context, message, 0);
    }

    public static void toast(Context context, String message, int length) {
        Toast.makeText(context, message, length).show();
    }


    public byte[] bytesOf(Uri imageUri) {
        try {
            InputStream fis = context.getContentResolver().openInputStream(imageUri);
            if (fis != null) {
                byte[] bytes = new byte[fis.available()];
                int read = fis.read(bytes);
                if (read > -1) return bytes;
                else toast("read <= -1");
            }
        } catch (IOException e) {
            //ToDo: show App Error Message
            toast(e.getMessage());
        }
        return null;
    }

    public static void clearET(EditText... ets) {
        if (ets != null && ets.length > 0) for (EditText et : ets) et.setText("");
    }

    public static String[] strArrOf(int id) {
        return new String[]{String.valueOf(id)};
    }


    public static String rndName() {
        return rndName(12);
    }

    public static String rndName(int n) {
        StringBuilder name = new StringBuilder();
        int len = (int) (Math.random() * n) + 3;

        for (int i = 0; i < len; i++) name.append((char) (Math.random() * 26 + 97));

        return name.toString();
    }

}
