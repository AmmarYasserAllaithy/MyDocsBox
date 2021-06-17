package com.example.ammaryasser.mydocsbox.ui.main.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.diffcallback.TagDiffCallback;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.viewholder.TagViewHolder;


public class TagAdapter extends ListAdapter<Tag, TagViewHolder> {

    public TagAdapter() {
        super(new TagDiffCallback());
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TagViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


//    review, code
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView;
//        if (convertView == null)
//              view = LayoutInflater.from(getContext()).inflate(R.layout.manage_item, parent, false);
//
//        int id = -1;
//        final Tag tag = getItem(position);
//
//        if (tag != null) {
//            id = tag.getId();
//            name.setText(tag.getTitle());
//        }
//        final int ID = id;
//
//        view.setOnClickListener(v -> {
////                    Toast.makeText(getContext(), ID + " - " + helper.selectTag(ID).getId(), Toast.LENGTH_SHORT).show();
//        });
//        editIB.setOnClickListener(v -> {
////                    Toast.makeText(getContext(), "Edit: " + helper.selectTag(ID).getName(), Toast.LENGTH_SHORT).show();
//        });
//        deleteIB.setOnClickListener(v -> {
//            //ToDo: check if tag used or not..
////            boolean del = tagDBHelper.delete(new Tag(ID));
////            Toast.makeText(getContext(), del ? "Deleted" : "Can't delete", Toast.LENGTH_SHORT).show();
//            notifyDataSetChanged();
//        });
//
//        view.setBackgroundResource(R.drawable.anim_bg_set_row);
//        return view;
//    }
}
