/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.ui.main.adapter.diffcallback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;


public class BookDiffCallback extends DiffUtil.ItemCallback<Book> {

    @Override
    public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
        return oldItem == newItem;
    }

}
