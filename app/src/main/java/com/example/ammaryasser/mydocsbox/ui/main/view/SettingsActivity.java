package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.ammaryasser.mydocsbox.ui.customviews.SettingLayout;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.BookAdapter;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.TagAdapter;
import com.example.ammaryasser.mydocsbox.ui.main.viewmodel.SettingsViewModel;
import com.example.ammaryasser.mydocsbox.util.Prefs;
import com.example.ammaryasser.mydocsbox.util.Prefs.Lang;
import com.example.ammaryasser.mydocsbox.util.Utils;

import java.util.Locale;

import static com.example.ammaryasser.mydocsbox.util.Prefs.getPrefs;


@SuppressWarnings("ALL")
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivityTAG";
    private static final int SELECT_DB_FILE_REQUEST = 1;

    private ActivitySettingsBinding binding;

    private Prefs prefs;

    private SettingsViewModel viewModel;

    private enum Type {Books, Tags}

    @StringRes
    private static final int[] VIEW_MODES = {R.string.view_mode_compact, R.string.view_mode_informative};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getPrefs(this);

        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.booksMLiveData.observe(this,
                books -> binding.settingBooks.setValue(getString(R.string.set_books_no, books.size())));

        viewModel.tagsMLiveData.observe(this,
                tags -> binding.settingTags.setValue(getString(R.string.set_tags_no, tags.size())));

        viewModel.updateBooksMLiveData(this);
        viewModel.updateTagsMLiveData(this);

        binding.settingLanguage.setValue(getString(R.string.set_gen_lang_value));
        binding.settingViewMode.setValue(getString(VIEW_MODES[prefs.getViewMode()]));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent refresh = new Intent(this, MainActivity.class);
//        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(refresh);
//
//        startActivity(new Intent(this, MainActivity.class));
    }


    /**
     * Setting Click Listeners
     */
    private void setupListeners() {
        binding.settingBooks.setOnSettingClicked(booksListener);
        binding.settingTags.setOnSettingClicked(tagsListener);
        binding.settingLanguage.setOnSettingClicked(languageListener);
        binding.settingDateFormat.setOnSettingClicked(dateFormatListener);
        binding.settingViewMode.setOnSettingClicked(viewModeListener);
        binding.settingTheme.setOnSettingClicked(themeListener);
        binding.settingFont.setOnSettingClicked(fontListener);
        binding.settingBackup.setOnSettingClicked(backupListener);
        binding.settingRestore.setOnSettingClicked(restoreListener);
        binding.settingAutoBackup.setOnSettingClicked(autoBackupListener);
        binding.settingFeedback.setOnSettingClicked(feedbackListener);
        binding.settingBugReport.setOnSettingClicked(bugReportListener);

        binding.aboutTv.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
    }


    private SettingLayout.SettingClickListener booksListener = () -> showDialog(Type.Books);

    private SettingLayout.SettingClickListener tagsListener = () -> showDialog(Type.Tags);

    private SettingLayout.SettingClickListener languageListener = () -> {
        setLocale(prefs.switchLang() == Lang.EN ? "en" : "ar");
        finish();
        recreate();
    };

    private SettingLayout.SettingClickListener dateFormatListener = () -> toast(getString(R.string.set_gen_date_key));

    private SettingLayout.SettingClickListener viewModeListener = () -> {
        binding.settingViewMode.setValue(getString(VIEW_MODES[prefs.switchViewMode()]));
        onBackPressed();
    };

    private SettingLayout.SettingClickListener themeListener = () -> toast(getString(R.string.set_apr_theme_key));

    private SettingLayout.SettingClickListener fontListener = () -> toast(getString(R.string.set_apr_font_key));

    private SettingLayout.SettingClickListener backupListener = () -> {
        // final String path = new BookDBHelper(this).backup(false);
        // if (path != null) toast("DB Backup created Successfully\n" + path);
        // else
        toast(getString(R.string.coming_soon));
    };

    private SettingLayout.SettingClickListener restoreListener = () -> {
        Intent restore = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(restore, "Select DB File"),
                SELECT_DB_FILE_REQUEST);
    };

    private SettingLayout.SettingClickListener autoBackupListener = () -> toast(getString(R.string.set_db_auto_backup_key));

    private SettingLayout.SettingClickListener feedbackListener = () -> toast(getString(R.string.set_feedback));

    private SettingLayout.SettingClickListener bugReportListener = () -> toast(getString(R.string.set_report_bug));


    // TODO 04-Aug-21 :-> refactor

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
//                    toast("DB Restored Successfully");
//                else toast("Restore Failed");
//
//            } else toast("Invalid Database File");
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


    /**
     * Dialogs
     */
    public void showDialog(final Type type) {
        final ManageListBinding binding = ManageListBinding.inflate(getLayoutInflater());

        binding.manageTitle.setText(type.toString());

        if (type == Type.Books) configBookAdapter(binding);
        else configTagAdapter(binding);


        binding.getRoot().setOnClickListener(v -> {
        });

        createDialog(
                binding.getRoot(),
                getString(R.string.cancel),
                null,
                getString(R.string.add_new),
                (dialog, which) -> insert(type))
                .show();
    }

    public void configBookAdapter(ManageListBinding binding) {
        viewModel.updateBooksMLiveData(this);

        final BookAdapter adapter = new BookAdapter();

        adapter.submitList(viewModel.booksMLiveData.getValue());
        binding.manageRecyclerView.setAdapter(adapter);
    }

    public void configTagAdapter(ManageListBinding binding) {
        viewModel.updateTagsMLiveData(this);

        final TagAdapter adapter = new TagAdapter();

        adapter.submitList(viewModel.tagsMLiveData.getValue());
        binding.manageRecyclerView.setAdapter(adapter);
    }

    public void insert(final Type type) {
        final ManageAddBinding binding = ManageAddBinding.inflate(getLayoutInflater());

        binding.manageAddHeader.setText(getString(R.string.m_add_new, type));

        createDialog(
                binding.getRoot(),
                getString(R.string.cancel),
                null,
                getString(R.string.add),
                (dialog, which) -> {
                    final String title = binding.mAddTitle.getText().toString();

                    if (!title.matches("[ ]*") && title.length() < 64)
                        if (type == Type.Books) insertBook(title);
                        else insertTag(title);
                })
                .show();
    }

    private void insertTag(String title) {
        TagRepository.getInstance(this).insertOrUpdate(new Tag(title)).subscribe(
                id -> toast("Added"),
                throwable -> toast("Can't Add Tag"));

        viewModel.updateTagsMLiveData(this);
    }

    private void insertBook(String title) {
        BookRepository.getInstance(this).insertOrUpdate(new Book(title)).subscribe(
                id -> toast("Added!"),
                throwable -> toast("Can't Add Book"));

        viewModel.updateBooksMLiveData(this);
    }


    private AlertDialog createDialog(
            final View root,
            final CharSequence negativeTxt,
            final DialogInterface.OnClickListener negativeListener,
            final CharSequence positiveTxt,
            final DialogInterface.OnClickListener positiveListener
    ) {
        return new AlertDialog.Builder(this)
                .setView(root)
                .setCancelable(false)
                .setNegativeButton(negativeTxt, negativeListener)
                .setPositiveButton(positiveTxt, positiveListener)
                .create();
    }


    /**
     * Toast
     */
    private void toast(int resId) {
        toast(getString(resId));
    }

    private void toast(String txt) {
        Utils.toast(getApplicationContext(), txt);
    }

}
