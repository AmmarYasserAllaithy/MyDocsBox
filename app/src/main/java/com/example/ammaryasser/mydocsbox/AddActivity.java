package com.example.ammaryasser.mydocsbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE_REQUEST = 1010;
    private static final int COLOR_PRIMARY = Color.parseColor("#008577");
    private ImageButton imageBtn;
    private EditText title_et, desc_et, tags_et, book_et;
    private Uri imageUri = null;
    private ArrayList<DBStructure.Tag> allTags, selectedTags;
    private ArrayList<DBStructure.Book> allBooks;
    private int selectedBookId;
    private ChipGroup tagsChipGroup, booksChipGroup;
    private DBHelper helper;
    private boolean isUpdate;
    private int docID;
    private DBStructure.Doc docToBeUpdated;
    private String editTagsIdx;
    private int editBookIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        imageBtn = findViewById(R.id.add_image);
        title_et = findViewById(R.id.add_title);
        desc_et = findViewById(R.id.add_desc);
        tags_et = findViewById(R.id.add_tags);
        book_et = findViewById(R.id.add_book);

        //ToDo: Minimize image button height if the src image is the default image.

        allTags = new ArrayList<>();
        allBooks = new ArrayList<>();

        tagsChipGroup = findViewById(R.id.tags_chip_group);
        booksChipGroup = findViewById(R.id.books_chip_group);
        booksChipGroup.setOnCheckedChangeListener(bookGroupListener);

        selectedTags = new ArrayList<>();
        selectedBookId = -1;

        helper = new DBHelper(this);
        docToBeUpdated = null;
        isUpdate = getIntent().getBooleanExtra("update", false);

        if (isUpdate) {
            docID = getIntent().getIntExtra("docID", -1);
            if (docID != -1) {
                Button add_button = findViewById(R.id.add_add_btn);
                add_button.setText(getString(R.string.bs_update));
                docToBeUpdated = helper.selectDoc(docID);
                byte[] imageBytes = docToBeUpdated.getImageBytes();
                if (imageBytes != null)
                    imageBtn.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                title_et.setText(docToBeUpdated.getTitle());
                desc_et.setText(docToBeUpdated.getDesc());
                editTagsIdx = docToBeUpdated.getTags();
                editBookIdx = docToBeUpdated.getBookId();
//                Log.d("indexes", '.' + editTagsIdx + '.'); ////////
            }
        }

        fillTags();
        fillBooks();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    this.imageUri = imageUri;
                    imageBtn.setImageURI(this.imageUri);
                }
            }
            if (type.startsWith("text/")) {
                String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (text != null) desc_et.setText(text);
            }
        }

    }

    private void fillTags() {
        tagsChipGroup.removeAllViews();
        DBHelper helper = new DBHelper(this);
        allTags = helper.getAllTags();
        helper.close();
        if (allTags != null && allTags.size() > 0)
            for (DBStructure.Tag tag : allTags)
                if (isUpdate && docToBeUpdated != null && editTagsIdx.contains(tag.getId() + ""))
                    addTagToChipGroup(tag, true);
                else addTagToChipGroup(tag, false);
    }

    private void addTagToChipGroup(DBStructure.Tag tag, boolean isSelected) {
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
        if (isSelected) chip.setChecked(true);
    }

    private void fillBooks() {
        booksChipGroup.removeAllViews();
        DBHelper helper = new DBHelper(this);
        allBooks = helper.getAllBooks();
        helper.close();
        if (allBooks != null && allBooks.size() > 0)
            for (DBStructure.Book book : allBooks)
                if (isUpdate && docToBeUpdated != null && book.getId() == editBookIdx)
                    addBookToChipGroup(book, true);
                else addBookToChipGroup(book, false);
    }

    private void addBookToChipGroup(DBStructure.Book book, boolean isSelected) {
        Chip chip = new Chip(booksChipGroup.getContext());
        chip.setId(book.getId());
        chip.setText(book.getTitle());
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setCheckedIconEnabled(false);
        chip.setTextColor(COLOR_PRIMARY);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorPrimary);
        chip.setChipCornerRadius(5);
        booksChipGroup.addView(chip);
        if (isSelected) chip.setChecked(true);
    }

    Chip.OnCheckedChangeListener tagCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Chip chip = (Chip) buttonView;
            if (isChecked) {
                selectedTags.add(new DBStructure.Tag(chip.getId(), chip.getText().toString()));
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
    ChipGroup.OnCheckedChangeListener bookGroupListener = new ChipGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(ChipGroup chipGroup, int i) {
            Chip chip = chipGroup.findViewById(i);
            if (chip != null) {
                selectedBookId = chip.getId();
                uncheckAllChips(chipGroup);
                chip.setClickable(false);
                chip.setChipBackgroundColorResource(R.color.colorPrimary);
                chip.setTextColor(Color.WHITE);
            }
        }
    };

    private void uncheckChip(Chip chip) {
        chip.setChecked(false);
        chip.setClickable(true);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextColor(COLOR_PRIMARY);
    }

    private void uncheckAllChips(ChipGroup chipGroup) {
        for (int id = 0; id < chipGroup.getChildCount(); ++id)
            uncheckChip((Chip) chipGroup.getChildAt(id));
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_REQUEST)
            imageBtn.setImageURI(imageUri = data.getData());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void addNewTag(View view) {
        String tag = tags_et.getText().toString();
        if (!tag.matches("[ ]*")) {
            DBHelper helper = new DBHelper(this);
            if (helper.insertTag(tag)) {
                DBStructure.Tag newTag = helper.getLastTag();
                if (allTags != null) allTags.add(newTag);
                else {
                    allTags = new ArrayList<>();
                    allTags.add(newTag);
                }
                addTagToChipGroup(newTag, true);
                tags_et.setText("");
            } else MainActivity.makeToast(this, "Can't insert into Tags Table", 1);
        } else MainActivity.makeToast(this, getString(R.string.valid_tag), 1);
    }

    public void addNewBook(View view) {
        String book = book_et.getText().toString();
        if (!book.matches("[ ]*")) {
            DBHelper helper = new DBHelper(this);
            if (helper.insertBook(book, null, null)) {
                DBStructure.Book newBook = helper.getLastBook();
                if (allBooks != null) allBooks.add(newBook);
                else {
                    allBooks = new ArrayList<>();
                    allBooks.add(newBook);
                }
                addBookToChipGroup(newBook, true);
                book_et.setText("");
            } else MainActivity.makeToast(this, "Can't insert into Books Table", 1);
        } else MainActivity.makeToast(this, getString(R.string.valid_book), 1);
    }

    public void add(View view) {
        String title = title_et.getText().toString();
        String desc = desc_et.getText().toString();
        byte[] imageBytes = imageUri == null ? null : imageToBytes(imageUri);
        if (isUpdate)
            imageBytes = imageUri == null ? docToBeUpdated.getImageBytes() : imageToBytes(imageUri);

        if (!(title.matches("[ ]*") || desc.matches("[ ]*")))
            if (selectedTags != null && selectedTags.size() > 0)
                if (selectedBookId != -1) {
                    StringBuilder tagsIdx = new StringBuilder();
                    for (int idx = 0; idx < selectedTags.size(); idx++)
                        tagsIdx.append(selectedTags.get(idx).getId()).append(" ");

                    boolean executed;
                    if (isUpdate)
                        executed = helper.updateDoc(docID, selectedBookId, title, desc, tagsIdx.toString(), imageBytes);
                    else
                        executed = helper.insertDoc(selectedBookId, title, desc, tagsIdx.toString(), imageBytes);
                    helper.close();

                    if (executed) {
                        if (isUpdate) startActivity(getParentActivityIntent());
                        else startActivity(new Intent(this, MainActivity.class));
                        clear();
                        imageUri = null;
                    }
                } else
                    MainActivity.makeToast(this, getString(R.string.select_book), 1);
            else
                MainActivity.makeToast(this, getString(R.string.select_tag), 1);
        else
            MainActivity.makeToast(this, getString(R.string.valid_msg), 1);

    }

    private byte[] imageToBytes(Uri imagePath) {
        try {
            InputStream fis = getContentResolver().openInputStream(imagePath);
            byte[] imageBytes = new byte[fis.available()];
            fis.read(imageBytes);
            return imageBytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearAll(View view) {
        clear();
    }

    private void clear() {
        imageBtn.setImageResource(R.drawable.ic_photo_camera_128dp);
        title_et.setText("");
        desc_et.setText("");
        tags_et.setText("");
        book_et.setText("");
        uncheckAllChips(tagsChipGroup);
        uncheckAllChips(booksChipGroup);
        selectedTags = new ArrayList<>();
        selectedBookId = -1;
    }
}