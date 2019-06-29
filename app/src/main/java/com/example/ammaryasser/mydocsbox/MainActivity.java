package com.example.ammaryasser.mydocsbox;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int themeLightColor = Color.parseColor("#DDEEEE");
    public static final String MyPrefsBox = "MyPrefsBox",
            LANG = "LANG",
            DATE_FORMAT = "DATE_FORMAT",
            VIEW_MODE = "VIEW_MODE",
            THEME = "THEME",
            FONT = "FONT",
            LAST_TAB_IDX = "LAST_TAB_IDX";
    public static int lang_ID, dateFormat_ID, viewMode_ID, theme_ID, font_ID, lastTab_ID;
    private static int exit = 0;
    private static SharedPreferences preferences;
    private DBHelper dbHelper;
    private static BottomSheet bottomSheet;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static ArrayList<DBStructure.Book> booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToDo: Take Permission to run on Android 6.0 or above

        preferences = getSharedPreferences(MyPrefsBox, Context.MODE_PRIVATE);
        lang_ID = preferences.getInt(LANG, 0);  //ToDo: Not completed yet
        dateFormat_ID = preferences.getInt(DATE_FORMAT, 0);  //ToDo: *
        viewMode_ID = preferences.getInt(VIEW_MODE, 0);
        theme_ID = preferences.getInt(THEME, 0);  //ToDo: *
        font_ID = preferences.getInt(FONT, 0);  //ToDo: *
        lastTab_ID = preferences.getInt(LAST_TAB_IDX, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(themeLightColor);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                preferences.edit().putInt(LAST_TAB_IDX, tab.getPosition()).apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dbHelper = new DBHelper(this);

        booksList = dbHelper.getAllBooks();
        if (booksList != null) {
            for (DBStructure.Book book : booksList) {
                TabLayout.Tab tab = tabLayout.newTab();
                tab.setText(book.getTitle());
                tab.setContentDescription(book.getTitle());
                tabLayout.addTab(tab);

                mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            }
        } else {
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(lastTab_ID);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (exit == 0) {
            makeToast(this, "Click again to exit", 0);
            exit++;
        } else {
            new DBHelper(this).onExitBackup();
            finish();
            moveTaskToBack(true);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                exit = 0;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search)
            startActivity(new Intent(this, SearchActivity.class));
        else if (id == R.id.action_archive) {
//            startActivity(new Intent(this, SearchActivity.class));
        } else if (id == R.id.action_settings)
            startActivity(new Intent(this, SettingsActivity.class));

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final ListView listView = rootView.findViewById(R.id.docs_list);

            Bundle args = getArguments();
            assert args != null;
            int currentTabIdx = args.getInt(ARG_SECTION_NUMBER);
            int bookId = booksList.get(currentTabIdx - 1).getId();
            ArrayList<DBStructure.Doc> docs = new DBHelper(getContext()).getAllDocs(bookId);
            if (docs != null) listView.setAdapter(new Adapter(docs));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    bs_Doc_ID = (int) l;
                    bottomSheet = new BottomSheet();
                    bottomSheet.show(getFragmentManager(), "BottomSheet");
                }
            });

            View view = getLayoutInflater().inflate(R.layout.list_footer, null);
            listView.addFooterView(view, null, false);

            return rootView;
        }

        class Adapter extends BaseAdapter {
            private ArrayList<DBStructure.Doc> docsList;

            Adapter(ArrayList<DBStructure.Doc> docsList) {
                this.docsList = docsList;
            }

            @Override
            public int getCount() {
                return docsList.size();
            }

            @Override
            public Object getItem(int i) {
                return docsList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return docsList.get(i).getId();
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                int layout = viewMode_ID == 0 ? R.layout.doc_compact_view : R.layout.doc_informative_view;
                @SuppressLint("ViewHolder")
                View row = getLayoutInflater().inflate(layout, viewGroup, false);

                final ImageView image = row.findViewById(R.id.doc_image);
                final TextView title = row.findViewById(R.id.doc_title);
                final TextView desc = row.findViewById(R.id.doc_desc);

                byte[] imageBytes = docsList.get(i).getImageBytes();
                if (imageBytes != null) {
                    image.setVisibility(View.VISIBLE);
                    RequestBuilder<Drawable> requestBuilder = Glide
                            .with(getContext())
                            .load(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                    if (viewMode_ID == 0) requestBuilder.circleCrop();
                    requestBuilder.into(image);
                }

                title.setText(docsList.get(i).getTitle());
                desc.setText(docsList.get(i).getDesc());

                if (viewMode_ID == 1) {
                    final TextView ts = row.findViewById(R.id.doc_create_ts);
                    ts.setText(docsList.get(i).getCreate_ts());
                }

                return row;
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return booksList == null ? 0 : booksList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }

    }

    /**
     * Bottom Sheet functions
     */
    private static int bs_Doc_ID = -1;

    public void bs_preview(View view) {
        startActivity(new Intent(this, DetailsActivity.class).putExtra("docId", bs_Doc_ID));
        bottomSheet.dismiss();
    }

    public void bs_copy(View view) {
        DBStructure.Doc doc = dbHelper.selectDoc(bs_Doc_ID);
        String docDetails = String.format("%s%n%n%s%n%n%s%n%n%s",
                doc.getTitle(), doc.getDesc(), doc.getCreate_ts(), doc.getUpdate_ts());
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("doc details", docDetails);
        clipboard.setPrimaryClip(clip);
        makeToast(this, "Copied!", 0);
        bottomSheet.dismiss();
    }

    public void bs_update(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("update", true).putExtra("docID", bs_Doc_ID);
        startActivity(intent);
        bottomSheet.dismiss();
    }

    public void bs_share(View view) {
        makeToast(this, "Share was clicked", 0);
        bottomSheet.dismiss();
    }

    public void bs_archive(View view) {
        makeToast(this, "Archive was clicked", 0);
        bottomSheet.dismiss();
    }

    public void bs_delete(View view) {
        boolean isDeleted = dbHelper.deleteDoc(bs_Doc_ID);
        if (isDeleted) {
            makeToast(this, "Deleted!", 0);
            recreate();
        }
        bottomSheet.dismiss();
    }

    public static class BottomSheet extends BottomSheetDialogFragment {
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.bs_layout, container, false);
        }
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy, HH:mm:ss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public static void makeToast(Context context, String message, int length) {
        Toast.makeText(context, message, length).show();
    }

}