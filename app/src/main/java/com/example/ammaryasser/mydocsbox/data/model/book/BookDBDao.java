package com.example.ammaryasser.mydocsbox.data.model.book;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface BookDBDao {

    @Insert(onConflict = REPLACE)
    Single<Long> insertOrUpdate(Book book);

    @Delete
    Completable delete(Book book);


    @Query("SELECT * FROM books WHERE id = :id")
    Single<Book> get(int id);

    @Query("SELECT * FROM books ORDER BY id DESC LIMIT 1")
    Single<Book> getLast();

    @Query("SELECT * FROM books")
    Single<List<Book>> getAll();

}
