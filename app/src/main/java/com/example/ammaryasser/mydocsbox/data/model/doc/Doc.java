package com.example.ammaryasser.mydocsbox.data.model.doc;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.util.Converters;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import static com.example.ammaryasser.mydocsbox.util.Constant.TABLE_DOCS;


@Entity(tableName = TABLE_DOCS,
        foreignKeys = @ForeignKey(
                entity = Book.class,
                parentColumns = "id",
                childColumns = "bookId",
                onDelete = ForeignKey.CASCADE))
@TypeConverters(Converters.class)
public class Doc implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(index = true)
    private int bookId;

    @Nullable
    private String imageUrl;

    private String title;
    private String desc;
    private List<Integer> tagIds;
    private long createTs = System.currentTimeMillis();
    private long updateTs;


    public Doc() {
    }

    public Doc(int bookId, @Nullable String imageUrl, String title, String desc, List<Integer> tagIds) {
        this.bookId = bookId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.tagIds = tagIds;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@Nullable String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(long createTs) {
        this.createTs = createTs;
    }

    public long getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(long updateTs) {
        this.updateTs = updateTs;
    }


    public String json() {
        return String.format(
                Locale.getDefault(),
                "{%n  id: `%d`,%n  book: `%d`,%n  title: `%s`,%n  desc: `%s`,%n  tags_ids: `%s`,%n  cr_ts: `%s`,%n  up_ts: `%s`%n}",
                getId(), getBookId(), getTitle(), getDesc(), getTagIds().toString(), getCreateTs(), getUpdateTs());
    }

}
