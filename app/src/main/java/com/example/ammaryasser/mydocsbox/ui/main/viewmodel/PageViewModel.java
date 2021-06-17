package com.example.ammaryasser.mydocsbox.ui.main.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.repository.DocRepository;

import java.util.List;


@SuppressWarnings("ALL")
public class PageViewModel extends ViewModel {

    public final MutableLiveData<List<Doc>> docsMLiveData = new MutableLiveData<>();


    public void updateDocsMLiveData(Context context, int bookId) {
        DocRepository.getInstance(context)
                .getAllInBook(bookId)
                .subscribe(docsMLiveData::postValue);
    }

}
