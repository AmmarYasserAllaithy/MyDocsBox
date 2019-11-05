package com.example.ammaryasser.mydocsbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammaryasser.mydocsbox.data_structure.Book;
import com.example.ammaryasser.mydocsbox.data_structure.Tag;

import java.util.ArrayList;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    private static final int SELECT_DB_FILE_REQUEST = 1;
    private SharedPreferences preferences;
    private TextView booksNo, tagsNo, vMode_TV, lang_TV;
    private CommonMethods com;
    private DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(MainActivity.MyPrefsBox, Context.MODE_PRIVATE);
        com = new CommonMethods(this);
        helper = new DBHelper(this);

        booksNo = findViewById(R.id.settings_books_no_tv);
        tagsNo = findViewById(R.id.settings_tags_no_tv);
        lang_TV = findViewById(R.id.set_gen_lang);
        vMode_TV = findViewById(R.id.set_apr_view_mode);

        booksNo.setText(getString(R.string.set_books_no, helper.getBooksNo()));
        tagsNo.setText(getString(R.string.set_tags_no, helper.getTagsNo()));
        lang_TV.setText(getString(R.string.set_gen_lang_value));
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
        showDialog("Books");
    }

    public void manageTags(View view) {
        showDialog("Tags");
    }

    public void setLang(View view) {
        MainActivity.lang_ID = MainActivity.lang_ID == 0 ? 1 : 0;
        setLocale(MainActivity.lang_ID == 0 ? "en" : "ar");
        lang_TV.setText(getString(R.string.set_gen_lang_value));
        preferences.edit().putInt(MainActivity.LANG, MainActivity.lang_ID).apply();
    }

    public void setDateFormat(View view) {
        com.makeToast("Date Format");
    }

    public void setViewMode(View view) {
        MainActivity.viewMode_ID = MainActivity.viewMode_ID == 0 ? 1 : 0;
        vMode_TV.setText(MainActivity.viewMode_ID == 0 ?
                getString(R.string.view_mode_compact) : getString(R.string.view_mode_informative));
        preferences.edit().putInt(MainActivity.VIEW_MODE, MainActivity.viewMode_ID).apply();
    }

    public void setTheme(View view) {
        com.makeToast("Theme");
    }

    public void setFont(View view) {
        com.makeToast("Font");
    }

    public void backup(View view) {
        DBHelper dbHelper = new DBHelper(this);
        String path = dbHelper.exportDatabase();
        if (path != null)
            com.makeToast("DB Backup Successfully\n" + path, 0);
        else com.makeToast("Backup Failed", 0);
    }

    public void restore(View view) {
        Intent restore = new Intent();
        restore.setType("*/*");
        restore.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(restore, "Select DB File"), SELECT_DB_FILE_REQUEST);
    }

    public void autoBackup(View view) {
        com.makeToast("Auto Backup");
    }

    public void feedback(View view) {
        com.makeToast("Feedback");
    }

    public void reportBug(View view) {
        com.makeToast("Report A Bug");
    }

    public void gotoAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
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
                    com.makeToast("DB Restored Successfully", 0);
                else com.makeToast("Restore Failed", 0);
            } else
                com.makeToast("Invalid Database File", 0);
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

//    public void makeDialog(String title, String message, String notes) {
//        View dialog = getLayoutInflater().inflate(R.layout.dialog, null);
//
//        TextView titleTV = dialog.findViewById(R.id.dialog_title);
//        TextView messageTV = dialog.findViewById(R.id.dialog_message);
//        TextView notesTV = dialog.findViewById(R.id.dialog_notes);
//        Button okBtn = dialog.findViewById(R.id.dialog_ok);
//        Button cancelBtn = dialog.findViewById(R.id.dialog_cancel);
//
//        titleTV.setText(title);
//        messageTV.setText(message);
//        notesTV.setText(notes);
//
//        final AlertDialog.Builder dialogBuilder = new AlertDialog
//                .Builder(this)
////                .setCancelable(false)
//                .setView(dialog);
//
//        final AlertDialog alertDialog = dialogBuilder.create();
//        Objects.requireNonNull(alertDialog
//                .getWindow())
//                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                .setBackgroundDrawableResource(android.R.color.transparent);
//
//        dialogBuilder.show();
//
//        okBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SettingsActivity.this, "OK", Toast.LENGTH_SHORT).show();
////                alertDialog.dismiss();
////                alertDialog.hide();
//            }
//        });
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SettingsActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
////                alertDialog.dismiss();
//            }
//        });
//
//    }

    public void showDialog(String type) {
        View view = getLayoutInflater().inflate(R.layout.list_manage, null);

        TextView title = view.findViewById(R.id.manage_title);
        title.setText(type);

        ListView listView = view.findViewById(R.id.manage_listView);
        listView.setAdapter(type.equalsIgnoreCase("Books") ?
                new BookAdapter(this, new DBHelper(this).getAllBooks()) :
                new TagAdapter(this, new DBHelper(this).getAllTags())
        );

        new AlertDialog.Builder(this).setView(view).show();
    }

}

class BookAdapter extends ArrayAdapter<Book> {

    private DBHelper helper = new DBHelper(getContext());

    public BookAdapter(@NonNull Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.item_manage, parent, false);

        TextView title = view.findViewById(R.id.item_manage_title);
        ImageButton editIB = view.findViewById(R.id.item_manage_edit);
        ImageButton deleteIB = view.findViewById(R.id.item_manage_delete);

        int id = -1;
        final Book book = getItem(position);

        if (book != null) {
            id = book.getId();
            title.setText(book.getTitle());
        }
        final int ID = id;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), ID + " - " + helper.selectBook(ID).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        editIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit: " + helper.selectBook(ID).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        deleteIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo: check if book used or not..
                boolean del = helper.deleteBook(ID);
                Toast.makeText(getContext(), del ? "Deleted" : "Can't delete", Toast.LENGTH_SHORT).show();
                //ToDo: delete from / refill listview
//                notifyDataSetChanged();
            }
        });

        view.setBackgroundResource(R.drawable.anim_bg_set_row);
        return view;
    }
}

class TagAdapter extends ArrayAdapter<Tag> {

    private DBHelper helper = new DBHelper(getContext());

    public TagAdapter(@NonNull Context context, ArrayList<Tag> tags) {
        super(context, 0, tags);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.item_manage, parent, false);

        TextView name = view.findViewById(R.id.item_manage_title);
        ImageButton editIB = view.findViewById(R.id.item_manage_edit);
        ImageButton deleteIB = view.findViewById(R.id.item_manage_delete);

        int id = -1;
        final Tag tag = getItem(position);

        if (tag != null) {
            id = tag.getId();
            name.setText(tag.getName());
        }
        final int ID = id;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), ID + " - " + helper.selectTag(ID).getId(), Toast.LENGTH_SHORT).show();
            }
        });
        editIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Edit: " + helper.selectTag(ID).getName(), Toast.LENGTH_SHORT).show();
            }
        });
        deleteIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo: check if tag used or not..
                boolean del = helper.deleteTag(ID);
                Toast.makeText(getContext(), del ? "Deleted" : "Can't delete", Toast.LENGTH_SHORT).show();
            }
        });

        view.setBackgroundResource(R.drawable.anim_bg_set_row);
        return view;
    }
}