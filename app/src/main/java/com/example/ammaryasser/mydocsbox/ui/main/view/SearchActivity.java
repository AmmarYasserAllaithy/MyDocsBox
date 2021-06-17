package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.databinding.ActivitySearchBinding;
import com.example.ammaryasser.mydocsbox.util.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private static final int COLOR_PRIMARY = Color.parseColor("#008577");

    private ActivitySearchBinding binding;

    private ArrayList<Tag> allTags, selectedTags;
    private ArrayList<Book> allBooks, selectedBooks;
//    private ChipGroup tagsChipGroup, booksChipGroup;

//    private BookDBHelper bookDbHelper;
//    private DocDBHelper docDBHelper;
//    private TagDBHelper tagDBHelper;

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        utils = new Utils(this);

        binding.searchBarEt.setOnClickListener(clickListener);
        binding.searchBarEt.addTextChangedListener(textWatcher);

        allTags = new ArrayList<>();
        allBooks = new ArrayList<>();

//        tagsChipGroup = findViewById(R.id.tags_chip_group);
//        booksChipGroup = findViewById(R.id.books_chip_group);
//        booksChipGroup.setOnCheckedChangeListener(bookGroupListener);

//        bookDbHelper = new BookDBHelper(this);
//        docDBHelper = new DocDBHelper(this);
//        tagDBHelper = new TagDBHelper(this);

        fillTags();
        fillBooks();

        selectedTags = new ArrayList<>();
        selectedBooks = new ArrayList<>();
    }


//    Chip.OnCheckedChangeListener tagCheckedChangeListener = (buttonView, isChecked) -> {
//        Chip chip = (Chip) buttonView;
//
//        if (isChecked) {
////            selectedTags.add(new Tag(chip.getId()));
//            chip.setChipBackgroundColorResource(R.color.colorPrimary);
//            chip.setTextColor(Color.WHITE);
//
//        } else {
//            for (int idx = 0; idx < selectedTags.size(); idx++)
//                if (selectedTags.get(idx).getId() == chip.getId()) {
//                    selectedTags.remove(idx);
//                    break;
//                }
//            uncheckChip(chip);
//        }
//    };

//    Chip.OnCheckedChangeListener bookCheckedChangeListener = (buttonView, isChecked) -> {
//        Chip chip = (Chip) buttonView;
//
//        if (isChecked) {
////            selectedBooks.add(new Book(chip.getId(), chip.getText().toString()));
//            chip.setChipBackgroundColorResource(R.color.colorPrimary);
//            chip.setTextColor(Color.WHITE);
//
//        } else {
//            for (int idx = 0; idx < selectedBooks.size(); idx++)
//                if (selectedBooks.get(idx).getId() == chip.getId()) {
//                    selectedBooks.remove(idx);
//                    break;
//                }
//            uncheckChip(chip);
//        }
//    };


    private void fillTags() {
        binding.tagsChipGroup.removeAllViews();
//        allTags = tagDBHelper.getAll();

        if (allTags != null && allTags.size() > 0)
            for (Tag tag : allTags)
                addTagToChipGroup(tag);
    }

    private void fillBooks() {
        binding.booksChipGroup.removeAllViews();
//        allBooks = bookDbHelper.getAll();

        if (allBooks != null && allBooks.size() > 0)
            for (Book book : allBooks)
                addBookToChipGroup(book);
    }


    private Chip createChip(Context context, int id, String title) {
        final Chip chip = new Chip(context);

        chip.setId(id);
        chip.setText(title);
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setTextColor(COLOR_PRIMARY);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorPrimary);
        chip.setCheckedIconEnabled(false);

        return chip;
    }


    private void addTagToChipGroup(Tag tag) {
        Chip chip = createChip(binding.tagsChipGroup.getContext(), tag.getId(), tag.getTitle());

//        chip.setOnCheckedChangeListener(tagCheckedChangeListener);

        binding.tagsChipGroup.addView(chip);
    }

    private void addBookToChipGroup(Book book) {
        Chip chip = createChip(binding.booksChipGroup.getContext(), book.getId(), book.getTitle());

//        chip.setOnCheckedChangeListener(bookCheckedChangeListener);
        chip.setChipCornerRadius(5);

        binding.booksChipGroup.addView(chip);
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


    EditText.OnClickListener clickListener = v -> binding.searchCriteria.setVisibility(View.VISIBLE);

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (binding.searchCriteria.getVisibility() == View.GONE)
                binding.searchCriteria.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void search(View view) {
        binding.searchList.setAdapter(null);
        binding.searchCriteria.setVisibility(View.GONE);
        String target = binding.searchBarEt.getText().toString();
        utils.toast("Search for: " + target);

//        ArrayList<Doc> result = docDBHelper.searchFor(target);
//        if (result != null) binding.searchList.setAdapter(new Adapter(result));

        binding.searchList.setOnItemClickListener((adapterView, v, i, l) -> startActivity(
                new Intent(SearchActivity.this, DetailsActivity.class)
                        .putExtra("docID", (int) l)
        ));
    }

    public void clearAll(View view) {
        clear();
    }

    private void clear() {
        binding.searchBarEt.setText("");
        uncheckAllChips(binding.tagsChipGroup);
        uncheckAllChips(binding.booksChipGroup);
        selectedTags = new ArrayList<>();
        selectedBooks = new ArrayList<>();
        binding.searchList.setAdapter(null);
    }


    class Adapter extends BaseAdapter {
        private ArrayList<Doc> docs;

        Adapter(ArrayList<Doc> docs) {
            this.docs = docs;
        }

        @Override
        public int getCount() {
            return docs.size();
        }

        @Override
        public Object getItem(int i) {
            return docs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return docs.get(i).getId();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder")
            View card = getLayoutInflater().inflate(R.layout.doc_card_search, viewGroup, false);

            final ImageView image = card.findViewById(R.id.doc_search_image);
            final TextView book = card.findViewById(R.id.doc_search_book);
            final TextView title = card.findViewById(R.id.doc_search_title);
            final TextView desc = card.findViewById(R.id.doc_search_desc);
            final TextView ts = card.findViewById(R.id.doc_search_create_ts);

//            byte[] imageBytes = docs.get(i).getImageBytes();
//            if (imageBytes != null) {
//                image.setVisibility(View.VISIBLE);
//                image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
//            }

//            Book book1 = bookDbHelper.get(docs.get(i).getBookId());
//            if (book1 != null) book.setText(book1.getTitle());
//            else {
//                book.setText("DELETED_BOOK");
//                book.setBackgroundResource(R.drawable.bg_doc_search_deleted_book);
//            }

            title.setText(docs.get(i).getTitle());
            desc.setText(docs.get(i).getDesc());
//            ts.setText(docs.get(i).getCreateTs());

            return card;
        }
    }
}