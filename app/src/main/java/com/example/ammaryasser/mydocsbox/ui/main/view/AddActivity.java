package com.example.ammaryasser.mydocsbox.ui.main.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.data.repository.BookRepository;
import com.example.ammaryasser.mydocsbox.data.repository.DocRepository;
import com.example.ammaryasser.mydocsbox.data.repository.TagRepository;
import com.example.ammaryasser.mydocsbox.databinding.ActivityAddBinding;
import com.example.ammaryasser.mydocsbox.ui.customviews.StyledChip;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.SectionsPagerAdapter;
import com.example.ammaryasser.mydocsbox.util.Prefs;
import com.example.ammaryasser.mydocsbox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.example.ammaryasser.mydocsbox.util.Constant.BOOK_CHIP_RADIUS;
import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_DOC;
import static com.example.ammaryasser.mydocsbox.util.Constant.EXTRA_EDIT;
import static com.example.ammaryasser.mydocsbox.util.Utils.clearET;
import static com.example.ammaryasser.mydocsbox.util.Utils.toast;


@SuppressWarnings("ALL")
public class AddActivity extends AppCompatActivity {

    private static final String TAG = "AddActivityTAG";
    private static final int SELECT_IMAGE_REQUEST = 1010;

    private ActivityAddBinding binding;

    private boolean isEdit;

    private Utils utils;

    private BookRepository bookRepo;
    private DocRepository docRepo;
    private TagRepository tagRepo;

    private Doc doc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        bookRepo = BookRepository.getInstance(this);
        docRepo = DocRepository.getInstance(this);
        tagRepo = TagRepository.getInstance(this);

        utils = new Utils(this);

        binding.attachIv.setOnClickListener(v -> openGallery());
        binding.iv.setOnClickListener(v -> openGallery());
        binding.clearIv.setOnClickListener(v -> clearImage());
        binding.addTagBtn.setOnClickListener(v -> insertTag());
        binding.addBookBtn.setOnClickListener(v -> insertBook());

        isEdit = getIntent().getBooleanExtra(EXTRA_EDIT, false);

        captureSharing();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isEdit) {
            doc = (Doc) getIntent().getSerializableExtra(EXTRA_DOC);
            binding.setDoc(doc);
        } else doc = new Doc();

        getAllBooks();
        getAllTags();
    }

    private void captureSharing() {
        final Intent intent = getIntent();
        final String action = intent.getAction();
        final String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null)
            if (type.startsWith("image/")) {
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

                if (imageUri != null) binding.iv.setImageURI(imageUri);

            } else if (type.startsWith("text/")) {
                String text = intent.getStringExtra(Intent.EXTRA_TEXT);

                if (text != null) binding.addDesc.setText(text);
            }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) insertDoc();
        else if (id == R.id.action_clear_all) clearAll();

        return super.onOptionsItemSelected(item);
    }


    private void openGallery() {
        Intent intent = new Intent()
                .setType("image/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_IMAGE_REQUEST) {
            Uri uri = data.getData();
            binding.iv.setImageURI(uri);
            doc.setImageUrl(uri.toString());

            Log.e(TAG, "onActivityResult: Uri" + uri + ", " + uri.toString());

            binding.attachIv.setText(getString(R.string.change_image));
            binding.clearIv.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Read from DB
     */
    private void getAllTags() {
        tagRepo.getAll().subscribe(
                tags -> reFillTagGroup(tags),
                e -> toast(this, "Error occur while get all tags!"));
    }

    private void getAllBooks() {
        bookRepo.getAll().subscribe(
                books -> reFillBookGroup(books),
                e -> toast(this, "Error occur while get all books!"));
    }


    /**
     * Write to DB
     */
    private void insertTag() {
        String tagName = binding.addTagEt.getText().toString();

        if (tagName.matches("[ ]*")) {
            utils.toast(getString(R.string.valid_tag));
            return;
        }


        tagRepo.insertOrUpdate(new Tag(tagName.trim())).subscribe(
                id -> tagRepo.get(Math.toIntExact(id)).subscribe(tag -> {

                    addTagChip(tag, true);
                    clearET(binding.addTagEt);

                }), e -> {
                    utils.toast("Can't insert Tags", 1);
                    Log.e(TAG, "Can't insert Tags: ", e);
                });
    }

    private void insertBook() {
        String name = binding.addBookEt.getText().toString();

        if (name.matches("[ ]*")) {
            utils.toast(getString(R.string.valid_book));
            return;
        }


        bookRepo.insertOrUpdate(new Book(name.trim())).subscribe(
                id -> bookRepo.get(Math.toIntExact(id)).subscribe(
                        book -> {

                            binding.booksChipGroup.clearCheck();
                            addBookChip(book, true);
                            clearET(binding.addBookEt);

                            SectionsPagerAdapter.addBookPos(book.getId());

                        }),
                e -> {
                    utils.toast("Can't insert Book", 1);
                    Log.e(TAG, "Can't insert Book: ", e);
                });
    }

    private void insertDoc() {
        final String title = binding.addTitle.getText().toString();
        final String desc = binding.addDesc.getText().toString();
        final List<Integer> tagIds = binding.tagsChipGroup.getCheckedChipIds();
        final int bookId = binding.booksChipGroup.getCheckedChipId();

        final StringBuilder errorMsg = new StringBuilder("");

        if (title.matches("[\\s]*")) append(errorMsg, R.string.add_title_hint);
        if (desc.matches("[\\s]*")) append(errorMsg, R.string.add_desc_hint);
        if (tagIds.isEmpty()) append(errorMsg, R.string.doc_tags);
        if (bookId == View.NO_ID) append(errorMsg, R.string.doc_book);

        if (!errorMsg.toString().equals("")) {
            errorMsg.append("are required");
            utils.toast(errorMsg.toString());
            return;
        }


        if (isEdit) doc.setUpdateTs(System.currentTimeMillis());

        // TODO 14-Jun-21 :-> Add image uri

        doc.setTitle(title);
        doc.setDesc(desc);
        doc.setTagIds(tagIds);
        doc.setBookId(bookId);

        docRepo.insertOrUpdate(doc).subscribe(() -> {
            if (!isEdit) toast(this, "Doc Inserted");

            Prefs.getPrefs(this).putLastPosition(SectionsPagerAdapter.getBookPos(bookId));

            onBackPressed(); // TODO 13-Jun-21 :-> Disable in multi insert

        }, e -> Log.e(TAG, "Can't insert Doc: ", e));

    }


    private StringBuilder append(StringBuilder sb, int resId) {
        return sb.append(getString(resId)).append('\n');
    }

    private void clearImage() {
        doc.setImageUrl(null);
        binding.iv.setImageDrawable(null);
        binding.attachIv.setText(getString(R.string.attach_image));
        binding.clearIv.setVisibility(View.GONE);
    }


    /**
     * Populate ChipGroup
     */
    private void reFillTagGroup(List<Tag> tags) {
        binding.tagsChipGroup.removeAllViews();

        for (Tag tag : tags)
            addTagChip(tag, isEdit &&
                    Arrays.binarySearch(binding.getDoc().getTagIds().toArray(), tag.getId()) >= 0);
    }

    private void reFillBookGroup(List<Book> books) {
        binding.booksChipGroup.removeAllViews();
        final int lastPosition = Prefs.getPrefs(this).getLastPosition();

        for (Book book : books)
            addBookChip(book, isEdit && binding.getDoc().getBookId() == book.getId()
                    || lastPosition == SectionsPagerAdapter.getBookPos(book.getId()));
    }

    private void addTagChip(Tag tag, boolean isChecked) {
        binding.tagsChipGroup.addView(new StyledChip(
                binding.tagsChipGroup.getContext(), tag.getId(), tag.getTitle(), isChecked));
    }

    private void addBookChip(Book book, boolean isChecked) {
        binding.booksChipGroup.addView(new StyledChip(
                binding.booksChipGroup.getContext(), book.getId(), book.getTitle(), isChecked)
                .radius(BOOK_CHIP_RADIUS));
    }


    private void clearAll() {
        clearImage();

        clearET(binding.addTitle, binding.addDesc, binding.addTagEt, binding.addBookEt);

        binding.tagsChipGroup.clearCheck();
        binding.booksChipGroup.clearCheck();
    }

}

