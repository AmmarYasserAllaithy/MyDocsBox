package com.example.ammaryasser.mydocsbox.ui.main.viewmodel;
/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.data.repository.BookRepository;
import com.example.ammaryasser.mydocsbox.data.repository.TagRepository;

import java.util.List;


@SuppressWarnings("ALL")
public class SettingsViewModel extends ViewModel {

    public final MutableLiveData<List<Book>> booksMLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<Tag>> tagsMLiveData = new MutableLiveData<>();


    public void updateBooksMLiveData(Context context) {
        BookRepository.getInstance(context).getAll().subscribe(booksMLiveData::postValue);
    }

    public void updateTagsMLiveData(Context context) {
        TagRepository.getInstance(context).getAll().subscribe(tagsMLiveData::postValue);
    }

}
