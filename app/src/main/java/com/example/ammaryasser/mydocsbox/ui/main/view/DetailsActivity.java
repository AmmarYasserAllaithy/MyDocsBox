package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.data.repository.BookRepository;
import com.example.ammaryasser.mydocsbox.data.repository.DocRepository;
import com.example.ammaryasser.mydocsbox.data.repository.TagRepository;
import com.example.ammaryasser.mydocsbox.databinding.ActivityDetailsBinding;
import com.example.ammaryasser.mydocsbox.ui.customviews.StyledChip;

import java.util.List;

import static com.example.ammaryasser.mydocsbox.util.Constant.BOOK_CHIP_RADIUS;
import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_DETAILS;
import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_DOC;
import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_EDIT;
import static com.example.ammaryasser.mydocsbox.util.Utils.toast;


@SuppressWarnings("ALL")
public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName() + "TAG";

    private ActivityDetailsBinding binding;

    private BookRepository bookRepo;
    private DocRepository docRepo;
    private TagRepository tagRepo;

    private Doc doc;
    private int docID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        bookRepo = BookRepository.getInstance(this);
        docRepo = DocRepository.getInstance(this);
        tagRepo = TagRepository.getInstance(this);

        doc = (Doc) getIntent().getSerializableExtra(EXTRA_DOC);
        docID = doc.getId();
    }

    @Override
    protected void onResume() {
        super.onResume();

        docRepo.get(docID).subscribe(doc -> {
            binding.setDoc(doc);

            reFillTagGroup(doc.getTagIds());
            reFillBookGroup(doc.getBookId());
        }, e -> {
            toast(this, "Can't retrieve doc!");
            Log.e(TAG, "onResume: ", e);
        });
    }

//    @Override
//    public void onBackPressed() {
//        //ToDo: learn more about stack
//        startActivity(getParentActivityIntent());
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit)
            startActivity(new Intent(this, AddActivity.class)
                    .putExtra(EXTRA_EDIT, true)
                    .putExtra(EXTRA_DETAILS, true)
                    .putExtra(EXTRA_DOC, doc));
        else if (id == R.id.action_export) toast(this, "Export was clicked");

        return super.onOptionsItemSelected(item);
    }


    private void reFillTagGroup(List<Integer> tagIds) {
        binding.tagsChipGroup.removeAllViews();
        tagRepo.getAllOf(tagIds).subscribe(tags -> tags.forEach(this::addTagChip));
    }

    private void reFillBookGroup(int id) {
        binding.booksChipGroup.removeAllViews();
        bookRepo.get(id).subscribe(this::addBookChip);
    }


    private void addTagChip(Tag tag) {
        StyledChip styledChip = new StyledChip(
                binding.tagsChipGroup.getContext(), tag.getId(), tag.getTitle(), false)
                .disabled();
        styledChip.setChipBackgroundColorResource(R.color.colorPrimaryLight);

        binding.tagsChipGroup.addView(styledChip);
    }

    private void addBookChip(Book book) {
        binding.booksChipGroup.addView(new StyledChip(
                binding.booksChipGroup.getContext(), book.getId(), book.getTitle(), true)
                .radius(BOOK_CHIP_RADIUS)
                .disabled());
    }

}