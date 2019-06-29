package com.example.ammaryasser.mydocsbox;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static final int COLOR_PRIMARY = Color.parseColor("#008577");
    private ChipGroup tagsChipGroup, booksChipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        int docID = getIntent().getIntExtra("docId", -1);
        DBHelper helper = new DBHelper(this);
        DBStructure.Doc doc = helper.selectDoc(docID);

        ImageView image = findViewById(R.id.details_image);
        TextView title_tv = findViewById(R.id.details_title);
        TextView desc_tv = findViewById(R.id.details_desc);
        TextView create_ts_tv = findViewById(R.id.details_create_ts);
        TextView update_ts_tv = findViewById(R.id.details_update_ts);

        byte[] imageBytes = doc.getImageBytes();
        if (imageBytes != null)
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        title_tv.setText(doc.getTitle());
        desc_tv.setText(doc.getDesc());
        create_ts_tv.setText(doc.getCreate_ts());
        update_ts_tv.setText(doc.getUpdate_ts() == null ? "Never Updated" : doc.getUpdate_ts());

        tagsChipGroup = findViewById(R.id.tags_chip_group);
        booksChipGroup = findViewById(R.id.books_chip_group);

        fillTags(doc.getTags());
        fillBook(doc.getBookId());
    }

    private void fillTags(String idx) {
        String[] indexes = idx.split(" ");
//        Log.d("indexes", Arrays.toString(indexes)); ///
        if (indexes.length > 0) {
            DBHelper helper = new DBHelper(this);
            for (String index : indexes) {
                String tagName = helper.selectTag(index).getName();
                addTagToChipGroup(tagName);
            }
            helper.close();
        }
    }

    private void addTagToChipGroup(String tagName) {
        Chip chip = new Chip(tagsChipGroup.getContext());
        chip.setText(tagName);
        chip.setCheckedIconEnabled(false);
        chip.setTextColor(COLOR_PRIMARY);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorPrimary);
        tagsChipGroup.addView(chip);
    }

    private void fillBook(int id) {
        DBHelper helper = new DBHelper(this);
        String bookTitle = helper.selectBook(id).getTitle();
        helper.close();

        Chip chip = new Chip(booksChipGroup.getContext());
        chip.setText(bookTitle);
        chip.setCheckedIconEnabled(false);
        chip.setTextColor(COLOR_PRIMARY);
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setRippleColorResource(R.color.colorPrimary);
        chip.setChipCornerRadius(5);
        booksChipGroup.addView(chip);
    }
}