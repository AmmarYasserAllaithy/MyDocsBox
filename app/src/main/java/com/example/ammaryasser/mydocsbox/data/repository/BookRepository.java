/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.data.repository;

import android.content.Context;

import com.example.ammaryasser.mydocsbox.data.model.MyDocsBoxDB;
import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.book.BookDBDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public final class BookRepository implements BookDBDao {

    private static BookRepository instance = null;
    private static BookDBDao dao = null;

    public static BookRepository getInstance(Context context) {
        if (instance == null) instance = new BookRepository();

        if (dao == null) dao = MyDocsBoxDB.getDatabase(context).bookDao();

        return instance;
    }


    public Single<Long> insertOrUpdate(Book book) {
        return dao.insertOrUpdate(book)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Book book) {
        return dao.delete(book)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Book> get(int id) {
        return dao.get(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Book> getLast() {
        return dao.getLast()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Book>> getAll() {
        return dao.getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
