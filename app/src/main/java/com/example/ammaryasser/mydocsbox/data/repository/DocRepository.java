/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.data.repository;

import android.content.Context;

import com.example.ammaryasser.mydocsbox.data.model.MyDocsBoxDB;
import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.model.doc.DocDBDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public final class DocRepository implements DocDBDao {

    private static DocRepository instance = null;
    private static DocDBDao dao = null;

    public static DocRepository getInstance(Context context) {
        if (instance == null) instance = new DocRepository();

        if (dao == null) dao = MyDocsBoxDB.getDatabase(context).docDao();

        return instance;
    }


    public Completable insertOrUpdate(Doc doc) {
        return dao.insertOrUpdate(doc)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Doc doc) {
        return dao.delete(doc)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> delete(int id) {
        return dao.delete(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Doc> get(int id) {
        return dao.get(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Doc> getLast() {
        return dao.getLast()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Doc>> getAll() {
        return dao.getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Doc>> getAllInBook(int bookId) {
        return dao.getAllInBook(bookId)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Doc>> getAllWithTag(int id) {
        return dao.getAllWithTag(id)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Doc>> getAllWithTags(int[] ids) {
        return dao.getAllWithTags(ids)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
