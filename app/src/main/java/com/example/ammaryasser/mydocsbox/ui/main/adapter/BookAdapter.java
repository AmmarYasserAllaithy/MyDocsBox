package com.example.ammaryasser.mydocsbox.ui.main.adapter;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.repository.BookRepository;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.diffcallback.BookDiffCallback;
import com.example.ammaryasser.mydocsbox.ui.main.adapter.viewholder.BookViewHolder;

import static com.example.ammaryasser.mydocsbox.util.Utils.toast;


@SuppressWarnings("ALL")
public class BookAdapter extends ListAdapter<Book, BookViewHolder> {

    public BookAdapter() {
        super(new BookDiffCallback());
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BookViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        final Book book = getItem(position);

        holder.bind(book,
                v -> {

                },
                v -> BookRepository.getInstance(v.getContext()).delete(book).subscribe(
                        () -> {
                            notifyDataSetChanged();
                            toast(v.getContext(), "Deleted!");
                        }, e -> {
                            toast(v.getContext(), "Can't Delete");
                            Log.e("BookAdapterTAG", "onBindViewHolder: ", e);
                        })
        );
    }


//    review, code
//    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = convertView;
//        if (convertView == null)
//            view = LayoutInflater.from(getContext()).inflate(R.layout.manage_item, parent, false);
//
//        int id = -1;
//        final Book book = getItem(position);
//
//        if (book != null) {
//            id = book.getId();
//            title.setText(book.getTitle());
//        }
//        final int ID = id;
//
//        view.setOnClickListener(v -> {
////                    Toast.makeText(getContext(), ID + " - " + helper.selectBook(ID).getId(), Toast.LENGTH_SHORT).show();
//        });
//        editIB.setOnClickListener(v -> {
////                    Toast.makeText(getContext(), "Edit: " + helper.selectBook(ID).getTitle(), Toast.LENGTH_SHORT).show();
//        });
//        deleteIB.setOnClickListener(v -> {
//            //ToDo: check if book used or not..
////            boolean del = helper.delete(new Book(ID));
////            Toast.makeText(getContext(), del ? "Deleted" : "Can't delete", Toast.LENGTH_SHORT).show();
//            //ToDo: delete from / refill listview
//
//        });
//
//        view.setBackgroundResource(R.drawable.anim_bg_set_row);
//        return view;
//    }
}
