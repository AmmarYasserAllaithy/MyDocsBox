package com.example.ammaryasser.mydocsbox.data.model.doc;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface DocDBDao {

    @Insert(onConflict = REPLACE)
    Completable insertOrUpdate(Doc doc);

    @Delete
    Completable delete(Doc doc);


    @Query("DELETE FROM docs WHERE id = :id")
    Single<Integer> delete(int id);

    @Query("SELECT * FROM docs WHERE id = :id")
    Single<Doc> get(int id);

    @Query("SELECT * FROM docs ORDER BY id DESC LIMIT 1")
    Single<Doc> getLast();

    @Query("SELECT * FROM docs")
    Single<List<Doc>> getAll();

    @Query("SELECT * FROM docs WHERE bookId = :bookId")
    Single<List<Doc>> getAllInBook(int bookId);

    @Query("SELECT * FROM docs WHERE tagIds IN (:id)")
    Single<List<Doc>> getAllWithTag(int id);

    @Query("SELECT * FROM docs WHERE tagIds IN (:ids)")
    Single<List<Doc>> getAllWithTags(int[] ids);

}
