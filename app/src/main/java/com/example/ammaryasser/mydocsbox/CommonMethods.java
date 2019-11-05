package com.example.ammaryasser.mydocsbox;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

public class CommonMethods {
    private Context CONTEXT;

    public CommonMethods() {
        CONTEXT = MainActivity.CONTEXT;
    }

    CommonMethods(Context context) {
        this.CONTEXT = context;
    }

    void copyToClipboard(String label, String text) {
        ((ClipboardManager) CONTEXT.getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(
                ClipData.newPlainText(label, text)
        );
        makeToast("Copied!");
    }

    void makeToast(String message) {
        makeToast(message, 0);
    }

    void makeToast(String message, int length) {
        Toast.makeText(CONTEXT, message, length).show();
    }
}
