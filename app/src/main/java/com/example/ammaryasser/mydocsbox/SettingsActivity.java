package com.example.ammaryasser.mydocsbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static final int SELECT_DB_FILE_REQUEST = 1;
    private SharedPreferences preferences;
    private TextView vMode_TV, lang_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(MainActivity.MyPrefsBox, Context.MODE_PRIVATE);

        lang_TV = findViewById(R.id.set_gen_lang);
        lang_TV.setText(getString(R.string.set_gen_lang_value));
        vMode_TV = findViewById(R.id.set_apr_view_mode);
        vMode_TV.setText(MainActivity.viewMode_ID == 0 ? getString(R.string.view_mode_compact) : getString(R.string.view_mode_informative));
    }

    @Override
    public void onBackPressed() {
//        Intent refresh = new Intent(this, MainActivity.class);
//        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(refresh);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void gotoProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void manageBooks(View view) {
        makeToast("Books");
        makeDialog("Manage Books", "Ur Books number = 4", "Test Dialog NOTES");
    }

    public void manageTags(View view) {
        makeToast("Tags");
    }

    public void setLang(View view) {
        MainActivity.lang_ID = MainActivity.lang_ID == 0 ? 1 : 0;
        setLocale(MainActivity.lang_ID == 0 ? "en" : "ar");
        lang_TV.setText(getString(R.string.set_gen_lang_value));
        preferences.edit().putInt(MainActivity.LANG, MainActivity.lang_ID).apply();
    }

    public void setDateFormat(View view) {
        makeToast("Date Format");
    }

    public void setViewMode(View view) {
        MainActivity.viewMode_ID = MainActivity.viewMode_ID == 0 ? 1 : 0;
        vMode_TV.setText(MainActivity.viewMode_ID == 0 ?
                getString(R.string.view_mode_compact) : getString(R.string.view_mode_informative));
        preferences.edit().putInt(MainActivity.VIEW_MODE, MainActivity.viewMode_ID).apply();
    }

    public void setTheme(View view) {
        makeToast("Theme");
    }

    public void setFont(View view) {
        makeToast("Font");
    }

    public void backup(View view) {
        DBHelper dbHelper = new DBHelper(this);
        String path = dbHelper.exportDatabase();
        if (path != null)
            MainActivity.makeToast(this, "DB Backup Successfully\n" + path, 0);
        else MainActivity.makeToast(this, "Backup Failed", 0);
    }

    public void restore(View view) {
        Intent restore = new Intent();
        restore.setType("*/*");
        restore.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(restore, "Select DB File"), SELECT_DB_FILE_REQUEST);
    }

    public void autoBackup(View view) {
        makeToast("Auto Backup");
    }

    public void feedback(View view) {
        makeToast("Feedback");
    }

    public void reportBug(View view) {
        makeToast("Report A Bug");
    }

    public void gotoAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    private void makeToast(String layout) {
        MainActivity.makeToast(this, layout + " was clicked", 0);
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang, "EG");
        Resources res = getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, metrics);
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refresh);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_DB_FILE_REQUEST) {
            String backupPath = getRealPathFromURI(data.getData());

            if (backupPath.endsWith(DBHelper.DB_NAME)) {
                DBHelper dbHelper = new DBHelper(this);
                boolean imported = dbHelper.importDatabase(backupPath);
                if (imported)
                    MainActivity.makeToast(this, "DB Restored Successfully", 0);
                else MainActivity.makeToast(this, "Restore Failed", 0);
            } else
                MainActivity.makeToast(this, "Invalid Database File", 0);
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public void makeDialog(String title, String message, String notes) {
        View dialog = getLayoutInflater().inflate(R.layout.dialog, null);

        TextView titleTV = dialog.findViewById(R.id.dialog_title);
        TextView messageTV = dialog.findViewById(R.id.dialog_message);
        TextView notesTV = dialog.findViewById(R.id.dialog_notes);

        titleTV.setText(title);
        messageTV.setText(message);
        notesTV.setText(notes);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialog);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

    }
}
