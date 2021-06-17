package com.example.ammaryasser.mydocsbox.data.model.tag;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface TagDBDao {

    @Insert(onConflict = REPLACE)
    Single<Long> insertOrUpdate(Tag tag);

    @Delete
    Completable delete(Tag tag);


    @Query("SELECT * FROM tags WHERE id = :id")
    Single<Tag> get(int id);

    @Query("SELECT * FROM tags ORDER BY id DESC LIMIT 1")
    Single<Tag> getLast();

    @Query("SELECT * FROM tags")
    Single<List<Tag>> getAll();

    @Query("SELECT * FROM tags WHERE id IN (:ids)")
    Single<List<Tag>> getAllOf(List<Integer> ids);

}
