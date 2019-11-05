package com.example.ammaryasser.mydocsbox;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ammaryasser.mydocsbox.data_structure.Book;
import com.example.ammaryasser.mydocsbox.data_structure.Doc;
import com.example.ammaryasser.mydocsbox.data_structure.Tag;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final int COLOR_PRIMARY = Color.parseColor("#008577");

    private EditText searchBar;
    private LinearLayout criteriaLayout;
    private ListView resultList;

    private ArrayList<Tag> allTags, selectedTags;
    private ArrayList<Book> allBooks, selectedBooks;
    private ChipGroup tagsChipGroup, booksChipGroup;

    private DBHelper helper;
    private CommonMethods com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        com = new CommonMethods(this);

        searchBar = findViewById(R.id.search_bar_et);
        searchBar.setOnClickListener(clickListener);
        searchBar.addTextChangedListener(textWatcher);
        criteriaLayout = findViewById(R.id.search_criteria);
        resultList = findViewById(R.id.search_list);

        allTags = new ArrayList<>();
        allBooks = new ArrayList<>();

        tagsChipGroup = findViewById(R.id.tags_chip_group);
        booksChipGroup = findViewById(R.id.books_chip_group);
//        booksChipGroup.setOnCheckedChangeListener(bookGroupListener);

        helper = new DBHelper(this);

        fillTags();
        fillBooks();

        selectedTags = new ArrayList<>();
        selectedBooks = new ArrayList<>();
    }

    Chip.OnCheckedChangeListener tagCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Chip chip = (Chip) buttonView;
            if (isChecked) {
                selectedTags.add(new Tag(chip.getId(), chip.getText().toString()));
                chip.setChipBackgroundColorResource(R.color.colorPrimary);
                chip.setTextColor(Color.WHITE);
            } else {
                for (int idx = 0; idx < selectedTags.size(); idx++)
                    if (selectedTags.get(idx).getId() == chip.getId()) {
                        selectedTags.remove(idx);
                        break;
                    }
                uncheckChip(chip);
            }
        }
    };
    Chip.OnCheckedChangeListener bookCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Chip chip = (Chip) buttonView;
            if (isChecked) {
                selectedBooks.add(new Book(chip.getId(), chip.getText().toString()));
                chip.setChipBackgroundColorResource(R.color.colorPrimary);
                chip.setTextColor(Color.WHITE);
            } else {
                for (int idx = 0; idx < selectedBooks.size(); idx++)
                    if (selectedBooks.get(idx).getId() == chip.getId()) {
                        selectedBooks.remove(idx);
                        break;
                    }
                uncheckChip(chip);
            }
        }
    };

    private void fillTags() {
        tagsChipGroup.removeAllViews();
        allTags = helper.getAllTags();
        if (allTags != null && allTags.size() > 0)
            for (Tag tag : allTags)
                addTagToChipGroup(tag);
    }

    private void addTagToChipGroup(Tag tag) {
        Chip chip = new Chip(tagsChipGroup.getContext());
        chip.setId(tag.getId());
        chip.setText(tag.getName());
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setOnCheckedChangeListener(tagCheckedChangeListener);
        chip.setCheckedIconEnabled(false);
        chip.setTextColor(COLOR_PRIMARY);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorPrimary);
        tagsChipGroup.addView(chip);
    }

    private void fillBooks() {
        booksChipGroup.removeAllViews();
        allBooks = helper.getAllBooks();
        if (allBooks != null && allBooks.size() > 0)
            for (Book book : allBooks)
                addBookToChipGroup(book);
    }

    private void addBookToChipGroup(Book book) {
        Chip chip = new Chip(booksChipGroup.getContext());
        chip.setId(book.getId());
        chip.setText(book.getTitle());
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setOnCheckedChangeListener(bookCheckedChangeListener);
        chip.setCheckedIconEnabled(false);
        chip.setTextColor(COLOR_PRIMARY);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorPrimary);
        chip.setChipCornerRadius(5);
        booksChipGroup.addView(chip);
    }

    private void uncheckAllChips(ChipGroup chipGroup) {
        for (int id = 0; id < chipGroup.getChildCount(); ++id)
            uncheckChip((Chip) chipGroup.getChildAt(id));
    }

    private void uncheckChip(Chip chip) {
        chip.setChecked(false);
        chip.setClickable(true);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextColor(COLOR_PRIMARY);
    }

    EditText.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            criteriaLayout.setVisibility(View.VISIBLE);
        }
    };
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (criteriaLayout.getVisibility() == View.GONE)
                criteriaLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void search(View view) {
        resultList.setAdapter(null);
        criteriaLayout.setVisibility(View.GONE);
        String target = searchBar.getText().toString();
        com.makeToast("Search for: " + target);

        ArrayList<Doc> result = helper.search(target);
        if (result != null) resultList.setAdapter(new Adapter(result));

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(SearchActivity.this, DetailsActivity.class).putExtra("docId", (int) l));
            }
        });
    }

    public void clearAll(View view) {
        clear();
    }

    private void clear() {
        searchBar.setText("");
        uncheckAllChips(tagsChipGroup);
        uncheckAllChips(booksChipGroup);
        selectedTags = new ArrayList<>();
        selectedBooks = new ArrayList<>();
        resultList.setAdapter(null);
    }

    class Adapter extends BaseAdapter {
        private ArrayList<Doc> resultList;

        Adapter(ArrayList<Doc> resultList) {
            this.resultList = resultList;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int i) {
            return resultList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return resultList.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = getLayoutInflater().inflate(R.layout.item_doc_search_view, viewGroup, false);

            final ImageView image = row.findViewById(R.id.doc_search_image);
            final TextView book = row.findViewById(R.id.doc_search_book);
            final TextView title = row.findViewById(R.id.doc_search_title);
            final TextView desc = row.findViewById(R.id.doc_search_desc);
            final TextView ts = row.findViewById(R.id.doc_search_create_ts);

            byte[] imageBytes = resultList.get(i).getImageBytes();
            if (imageBytes != null) {
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
            }
            book.setText(helper.getBookTitleOf(resultList.get(i).getBookId()));
            title.setText(resultList.get(i).getTitle());
            desc.setText(resultList.get(i).getDesc());
            ts.setText(resultList.get(i).getCreate_ts());

            return row;
        }
    }
}