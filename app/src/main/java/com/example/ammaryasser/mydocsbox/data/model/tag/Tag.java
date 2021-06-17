package com.example.ammaryasser.mydocsbox.data.model.tag;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

import static com.example.ammaryasser.mydocsbox.util.Constant.TABLE_TAGS;


@Entity(tableName = TABLE_TAGS)
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private long createTs = System.currentTimeMillis();


    public Tag(String title) {
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
                getId(), getTitle(), getCreateTs());
    }

}
