/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.util;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ammaryasser.mydocsbox.R;
import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;

import static com.example.ammaryasser.mydocsbox.util.Utils.formatDate;


public class BindingUtil {
    public static final String TAG = BindingUtil.class.getSimpleName() + "TAG";


    @BindingAdapter("doc_image")
    public static void loadDocImage(ImageView view, Doc doc) {

        if (doc != null && doc.getImageUrl() != null && !doc.getImageUrl().isEmpty())

            Glide.with(view.getContext())
                    .load(Uri.parse(doc.getImageUrl()))
//                    .load(R.drawable.tahrir_square)
                    .error(R.drawable.ic_outline_account_circle_24)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(view);

        else view.setVisibility(View.GONE);
    }

    @BindingAdapter("doc_title")
    public static void setDocTitle(TextView view, Doc doc) {
        if (doc != null) view.setText(doc.getTitle());
    }

    @BindingAdapter("doc_desc")
    public static void setDocDesc(TextView view, Doc doc) {
        if (doc != null) view.setText(doc.getDesc());
    }

    @BindingAdapter("doc_create_ts")
    public static void setDocCreateTs(TextView view, Doc doc) {
        if (doc != null) view.setText(formatDate(doc.getCreateTs()));
    }

    @BindingAdapter("doc_update_ts")
    public static void setDocUpdateTs(TextView view, Doc doc) {
        if (doc != null && doc.getUpdateTs() != 0) {
            view.setVisibility(View.VISIBLE);
            view.setText(formatDate(doc.getUpdateTs()));
        }
    }


    /**
     * Books
     */
    @BindingAdapter("book_title")
    public static void setBookTitle(TextView view, Book book) {
        if (book != null) view.setText(book.getTitle());
    }

    /**
     * Tag
     */
    @BindingAdapter("tag_title")
    public static void setTagTitle(TextView view, Tag tag) {
        if (tag != null) view.setText(tag.getTitle());
    }

}
