/*
 * Author   : Ammar Yasser AllaiThy
 * Website  : https://ammaryasserallaithy.github.io
 * Portfolio: https://ammaryasser.netlify.app
 */

package com.example.ammaryasser.mydocsbox.data.repository;

import android.content.Context;

import com.example.ammaryasser.mydocsbox.data.model.MyDocsBoxDB;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.data.model.tag.TagDBDao;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public final class TagRepository implements TagDBDao {

    private static TagRepository instance = null;
    private static TagDBDao dao = null;

    public static TagRepository getInstance(Context context) {
        if (instance == null) instance = new TagRepository();

        if (dao == null) dao = MyDocsBoxDB.getDatabase(context).tagDao();

        return instance;
    }


    public Single<Long> insertOrUpdate(Tag tag) {
        return dao.insertOrUpdate(tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable delete(Tag tag) {
        return dao.delete(tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<Tag> get(int id) {
        return dao.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Tag> getLast() {
        return dao.getLast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Tag>> getAll() {
        return dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Tag>> getAllOf(List<Integer> tagIds) {
        return dao.getAllOf(tagIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
