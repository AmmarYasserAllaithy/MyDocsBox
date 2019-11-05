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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ammaryasser.mydocsbox.data_structure.Book;
import com.example.ammaryasser.mydocsbox.data_structure.Doc;
import com.example.ammaryasser.mydocsbox.data_structure.Tag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private static final int
            SELECT_IMAGE_REQUEST = 1010,
            COLOR_PRIMARY = Color.parseColor("#008577");
    private ImageView iv;
    private TextView attachImg_TV, clearImg_TV;
    private EditText title_ET, desc_ET, tags_ET, book_ET;
    private Uri imageUri = null;
    private ArrayList<Tag> allTags, selectedTags;
    private ArrayList<Book> allBooks;
    private ChipGroup tagsChipGroup, booksChipGroup;
    private DBHelper helper;
    private boolean typeUpdate;
    private Doc docToBeUpdated;
    private String editTagsIdx;
    private int
            selectedBookId,
            docID,
            editBookIdx;
    private CommonMethods com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        com = new CommonMethods(this);

        iv = findViewById(R.id.iv);
        attachImg_TV = findViewById(R.id.attach_image);
        clearImg_TV = findViewById(R.id.clear_image);
        title_ET = findViewById(R.id.add_title);
        desc_ET = findViewById(R.id.add_desc);
        tags_ET = findViewById(R.id.add_tags);
        book_ET = findViewById(R.id.add_book);

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
        typeUpdate = getIntent().getBooleanExtra("update", false);

        if (typeUpdate) {
            docID = getIntent().getIntExtra("docID", -1);
            if (docID != -1) {
                docToBeUpdated = helper.selectDoc(docID);
                byte[] imageBytes = docToBeUpdated.getImageBytes();
                if (imageBytes != null) {
                    iv.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
                    attachImg_TV.setText(getString(R.string.change_image));
                    clearImg_TV.setVisibility(View.VISIBLE);
                }
                title_ET.setText(docToBeUpdated.getTitle());
                desc_ET.setText(docToBeUpdated.getDesc());
                editTagsIdx = docToBeUpdated.getTags();
                editBookIdx = docToBeUpdated.getBookId();
            }
        }

        fillTags();
        fillBooks();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null)
            if (type.startsWith("image/")) {
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) iv.setImageURI(this.imageUri = imageUri);

            } else if (type.startsWith("text/")) {
                String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (text != null) desc_ET.setText(text);
            }
    }

    private void fillTags() {
        tagsChipGroup.removeAllViews();
        DBHelper helper = new DBHelper(this);
        allTags = helper.getAllTags();
        helper.close();
        if (allTags != null && allTags.size() > 0)
            for (Tag tag : allTags)
                if (typeUpdate && docToBeUpdated != null && editTagsIdx.contains(tag.getId() + ""))
                    addTagToChipGroup(tag, true);
                else addTagToChipGroup(tag, false);
    }

    private void addTagToChipGroup(Tag tag, boolean isSelected) {
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
            for (Book book : allBooks)
                if (typeUpdate && docToBeUpdated != null && book.getId() == editBookIdx)
                    addBookToChipGroup(book, true);
                else addBookToChipGroup(book, false);
    }

    private void addBookToChipGroup(Book book, boolean isSelected) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) add(null);
        else if (id == R.id.action_clear_all) clearAll();


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_REQUEST) {
            iv.setImageURI(imageUri = data.getData());
            attachImg_TV.setText(getString(R.string.change_image));
            clearImg_TV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void addNewTag(View view) {
        String tag = tags_ET.getText().toString();
        if (!tag.matches("[ ]*")) {
            DBHelper helper = new DBHelper(this);
            if (helper.insertTag(tag)) {
                Tag newTag = helper.getLastTag();
                if (allTags != null) allTags.add(newTag);
                else {
                    allTags = new ArrayList<>();
                    allTags.add(newTag);
                }
                addTagToChipGroup(newTag, true);
                tags_ET.setText("");
            } else com.makeToast("Can't insert into Tags Table", 1);
        } else com.makeToast(getString(R.string.valid_tag), 1);
    }

    public void addNewBook(View view) {
        String book = book_ET.getText().toString();
        if (!book.matches("[ ]*")) {
            DBHelper helper = new DBHelper(this);
            if (helper.insertBook(book)) {
                Book newBook = helper.getLastBook();
                if (allBooks != null) allBooks.add(newBook);
                else {
                    allBooks = new ArrayList<>();
                    allBooks.add(newBook);
                }
                addBookToChipGroup(newBook, true);
                book_ET.setText("");
            } else com.makeToast("Can't insert into Books Table", 1);
        } else com.makeToast(getString(R.string.valid_book), 1);
    }

    //ToDo: remove image when update from DB
    public void add(View view) {
        String title = title_ET.getText().toString();
        String desc = desc_ET.getText().toString();
        byte[] imageBytes = imageUri == null ? null : imageToBytes(imageUri);
        if (typeUpdate)
            imageBytes = imageUri == null ? docToBeUpdated.getImageBytes() : imageToBytes(imageUri);

        if (!(title.matches("[ ]*") || desc.matches("[ ]*")))
            if (selectedTags != null && selectedTags.size() > 0)
                if (selectedBookId != -1) {
                    StringBuilder tagsIdx = new StringBuilder();
                    for (int idx = 0; idx < selectedTags.size(); idx++)
                        tagsIdx.append(selectedTags.get(idx).getId()).append(" ");

                    boolean executed;
                    if (typeUpdate)
                        executed = helper.updateDoc(docID, selectedBookId, title, desc, tagsIdx.toString(), imageBytes);
                    else
                        executed = helper.insertDoc(selectedBookId, title, desc, tagsIdx.toString(), imageBytes);
                    helper.close();

                    if (executed) {
                        if (typeUpdate) startActivity(getParentActivityIntent());
                        else startActivity(new Intent(this, MainActivity.class));
                        clearAll();
                        imageUri = null;
                    }
                } else com.makeToast(getString(R.string.select_book), 1);
            else com.makeToast(getString(R.string.select_tag), 1);
        else com.makeToast(getString(R.string.valid_msg), 1);

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

    public void clearImage(View view) {
        imageUri = null;
        iv.setImageDrawable(null);
        attachImg_TV.setText(getString(R.string.attach_image));
        clearImg_TV.setVisibility(View.GONE);
    }

    private void clearAll() {
        clearImage(null);
        title_ET.setText("");
        desc_ET.setText("");
        tags_ET.setText("");
        book_ET.setText("");
        uncheckAllChips(tagsChipGroup);
        uncheckAllChips(booksChipGroup);
        selectedTags = new ArrayList<>();
        selectedBookId = -1;
    }
}