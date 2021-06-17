package com.example.ammaryasser.mydocsbox.data.model.book;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

import static com.example.ammaryasser.mydocsbox.util.Constant.TABLE_BOOKS;
import static com.example.ammaryasser.mydocsbox.util.Utils.formatDate;


@Entity(tableName = TABLE_BOOKS)
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private long createTs = System.currentTimeMillis();


    public Book(String title) {
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(long createTs) {
        this.createTs = createTs;
    }


    public String json() {
        return String.format(
                Locale.getDefault(),
                "{%n  id: `%d`,%n  title: `%s`,%n  crts: `%s`%n}",
                getId(), getTitle(), formatDate(getCreateTs()));
    }

}
