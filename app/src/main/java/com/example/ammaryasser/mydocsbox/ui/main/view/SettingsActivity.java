package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.data.repository.BookRepository;
import com.example.ammaryasser.mydocsbox.data.repository.TagRepository;
import com.example.ammaryasser.mydocsbox.databinding.ActivitySettingsBinding;
import com.example.ammaryasser.mydocsbox.databinding.ManageAddBinding;
import com.example.ammaryasser.mydocsbox.databinding.ManageListBinding;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.BookAdapter;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.TagAdapter;
import com.example.ammaryasser.mydocsbox.ui.main.viewmodel.SettingsViewModel;
import com.example.ammaryasser.mydocsbox.util.Prefs;
import com.example.ammaryasser.mydocsbox.util.Prefs.Lang;
import com.example.ammaryasser.mydocsbox.util.Prefs.ViewMode;
import com.example.ammaryasser.mydocsbox.util.Utils;

import java.util.Locale;

import static com.example.ammaryasser.mydocsbox.util.Prefs.getPrefs;


@SuppressWarnings("ALL")
public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = SettingsActivity.class.getSimpleName() + "TAG";
    private static final int SELECT_DB_FILE_REQUEST = 1;

    private ActivitySettingsBinding binding;

    private Prefs prefs;
    private Utils utils;

    private SettingsViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        binding.username.setText(androidId);

        prefs = getPrefs(this);
        utils = new Utils(this);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.booksMLiveData.observe(this, books ->
                binding.settingsBooksNoTv.setText(getString(R.string.set_books_no, books.size())));

        viewModel.tagsMLiveData.observe(this, tags ->
                binding.settingsTagsNoTv.setText(getString(R.string.set_tags_no, tags.size())));

        viewModel.updateBooksMLiveData(this);
        viewModel.updateTagsMLiveData(this);

        binding.setGenLang.setText(getString(R.string.set_gen_lang_value));
        binding.setAprViewMode.setText(prefs.getViewMode() == ViewMode.COMPACT ? getString(R.string.view_mode_compact) : getString(R.string.view_mode_informative));
    }

//    @Override
//    public void onBackPressed() {
//        Intent refresh = new Intent(this, MainActivity.class);
//        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(refresh);
//
//        startActivity(new Intent(this, MainActivity.class));
//    }


    public void gotoProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void manageBooks(View view) {
        showBooks();
    }

    public void manageTags(View view) {
        showTags();
    }

    public void setLang(View view) {
        setLocale(prefs.switchLang() == Lang.EN ? "en" : "ar");
        recreate();
        binding.setGenLang.setText(getString(R.string.set_gen_lang_value));
    }

    public void setDateFormat(View view) {
        utils.toast("Date Format");
    }


    @StringRes
    private static final int[] VIEW_MODES = {R.string.view_mode_compact, R.string.view_mode_informative};

    public void setViewMode(View view) {
        binding.setAprViewMode.setText(getString(VIEW_MODES[prefs.switchViewMode()]));
        onBackPressed();
    }


    public void setTheme(View view) {
        utils.toast("Theme");
    }

    public void setFont(View view) {
        utils.toast("Font");
    }

    public void backup(View view) {
//        final String path = new BookDBHelper(this).backup(false);
//        if (path != null) utils.toast("DB Backup created Successfully\n" + path);
//        else
        utils.toast("Coming soon");
    }

    public void restore(View view) {
        Intent restore = new Intent();
        restore.setType("*/*");
        restore.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(restore, "Select DB File"), SELECT_DB_FILE_REQUEST);
    }

    public void autoBackup(View view) {
        utils.toast("Auto Backup");
    }

    public void feedback(View view) {
        utils.toast("Feedback");
    }

    public void reportBug(View view) {
        utils.toast("Report A Bug");
    }

    public void gotoAbout(View view) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang, lang.equals("ar") ? "EG" : "US");
        Resources res = getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, metrics);

//        Intent refresh = new Intent(this, MainActivity.class);
//        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(refresh);
//        recreate();

//        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_DB_FILE_REQUEST) {
            String backupPath = getRealPathFromURI(data.getData());

//            if (backupPath.endsWith(DBHelper.DB_NAME)) {
//
//                if (new BookDBHelper(this).restore(backupPath))
//                    utils.toast("DB Restored Successfully");
//                else utils.toast("Restore Failed");
//
//            } else utils.toast("Invalid Database File");
        }
    }

    public String getRealPathFromURI(Uri uri) {
        //todo: review later
        Cursor cursor = null;

        try {
            final String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, proj, null, null, null);
            final int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);

        } finally {
            if (cursor != null) cursor.close();
        }
    }

    public void showBooks() {
        showDialog("Books");
    }

    public void showTags() {
        showDialog("Tags");
    }

    public void showDialog(final String type) {
        ManageListBinding binding = ManageListBinding.inflate(getLayoutInflater());

        binding.manageTitle.setText(type);

        viewModel.updateBooksMLiveData(this);
        viewModel.updateTagsMLiveData(this);

        final BookAdapter bookAdapter = new BookAdapter();
        final TagAdapter tagAdapter = new TagAdapter();

        bookAdapter.submitList(viewModel.booksMLiveData.getValue());
        tagAdapter.submitList(viewModel.tagsMLiveData.getValue());

        binding.manageRecyclerView.setAdapter(type.equals("Books") ? bookAdapter : tagAdapter);

        new AlertDialog
                .Builder(this)
                .setView(binding.getRoot())
                .setCancelable(false)
                .setPositiveButton("Close", null)
                .setNegativeButton("Add New", (dialog, which) -> {

                    if (type.equalsIgnoreCase("Books")) showAddNew(getString(R.string.doc_book));
                    else showAddNew(getString(R.string.doc_tag));
                })
                .show();
    }

    public void showAddNew(final String type) {
        ManageAddBinding binding = ManageAddBinding.inflate(getLayoutInflater());

        binding.manageAddHeader.setText(getString(R.string.m_add_new, type));

        new AlertDialog
                .Builder(this)
                .setView(binding.getRoot())
                .setCancelable(false)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {

                    String title = binding.mAddTitle.getText().toString();

                    if (!title.matches("[ ]*") && title.length() < 64)

                        if (type.equals(getString(R.string.doc_book))) insertBook(title);
                        else insertTag(title);
                })
                .show();
    }

    private void insertTag(String title) {
        TagRepository.getInstance(this).insertOrUpdate(new Tag(title)).subscribe(
                id -> utils.toast("Added"),
                throwable -> utils.toast("Can't Add Tag"));

        viewModel.updateTagsMLiveData(this);
    }

    private void insertBook(String title) {
        BookRepository.getInstance(this).insertOrUpdate(new Book(title)).subscribe(
                id -> utils.toast("Added!"),
                throwable -> utils.toast("Can't Add Book"));

        viewModel.updateBooksMLiveData(this);
    }

}
