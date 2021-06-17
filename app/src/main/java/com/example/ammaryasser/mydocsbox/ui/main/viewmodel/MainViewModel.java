package com.example.ammaryasser.mydocsbox.ui.main.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.repository.BookRepository;

import java.util.List;


@SuppressWarnings("ALL")
public class MainViewModel extends ViewModel {

    public final MutableLiveData<List<Book>> booksMLiveData = new MutableLiveData<>();


    public void updateBooksMLiveData(Context context) {
        BookRepository.getInstance(context)
                .getAll()
                .subscribe(booksMLiveData::postValue);
    }

}
