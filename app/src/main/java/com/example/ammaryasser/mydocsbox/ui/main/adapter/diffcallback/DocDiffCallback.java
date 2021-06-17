/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.ui.main.adapter.diffcallback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;


public class DocDiffCallback extends DiffUtil.ItemCallback<Doc> {

    @Override
    public boolean areItemsTheSame(@NonNull Doc oldItem, @NonNull Doc newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Doc oldItem, @NonNull Doc newItem) {
        return oldItem == newItem;
    }

}
