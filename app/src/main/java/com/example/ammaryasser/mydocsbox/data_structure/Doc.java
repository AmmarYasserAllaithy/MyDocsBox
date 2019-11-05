package com.example.ammaryasser.mydocsbox.data_structure;

public class Doc {

    private int id, bookId;
    private String title, desc, tags, create_ts, update_ts;
    byte[] imageBytes;

    public Doc(int id, int bookId, String title, String desc, String tags, byte[] imageBytes, String create_ts, String update_ts) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.desc = desc;
        this.tags = tags;
        this.imageBytes = imageBytes;
        this.create_ts = create_ts;
        this.update_ts = update_ts;
    }

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getTags() {
        return tags;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public String getCreate_ts() {
        return create_ts;
    }

    public String getUpdate_ts() {
        return update_ts;
    }
}
