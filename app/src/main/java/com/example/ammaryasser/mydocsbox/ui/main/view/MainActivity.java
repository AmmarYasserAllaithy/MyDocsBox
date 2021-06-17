package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.databinding.ActivityMainBinding;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.SectionsPagerAdapter;
import com.example.ammaryasser.mydocsbox.ui.main.viewmodel.MainViewModel;
import com.example.ammaryasser.mydocsbox.util.Prefs;
import com.google.android.material.tabs.TabLayout;

import static com.example.ammaryasser.mydocsbox.util.Utils.toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityTAG";
    private static final int PERMISSIONS_REQUEST = 1001;

    private static boolean exit = false;

    private ActivityMainBinding binding;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        grantPermissions();

        setupViewPager();
        setupListeners();
        setupViewModel();

        binding.fab.setOnClickListener(v -> goTo(AddActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        mainViewModel.updateBooksMLiveData(this);

        new Handler().postDelayed(
                () -> binding.viewPager.setCurrentItem(Prefs.getPrefs(this).getLastPosition()),
                300);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) goTo(SearchActivity.class);

        else if (item.getItemId() == R.id.action_archive) goTo(ProfileActivity.class);

        else if (item.getItemId() == R.id.action_settings) goTo(SettingsActivity.class);

        else super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!exit) {
            toast(this, "Click again to exit", 0);
            exit = !exit;

        } else {
            // TODO 13-Jun-21 :-> backup
            finish();
        }

        new Handler().postDelayed(() -> exit = false, 2_000);
    }


    private void grantPermissions() {
        final String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET};

        for (String permission : permissions)
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
    }

    private void setupViewPager() {
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        binding.viewPager.setAdapter(sectionsPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void setupListeners() {
        binding.tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(binding.viewPager));

        binding.viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout) {

                    @Override
                    public void onPageSelected(int position) {
                        Prefs.getPrefs(getApplicationContext()).putLastPosition(position);
                    }
                });
    }

    private void setupViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.booksMLiveData.observe(this, sectionsPagerAdapter::setBooks);
    }

    private void goTo(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

}
