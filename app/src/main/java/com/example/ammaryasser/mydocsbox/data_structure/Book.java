package com.example.ammaryasser.mydocsbox.data_structure;

public class Book {

    private int id;
    private String title, desc, color;

    public Book(int id, String title, String desc, String color) {
        this(id, title);
        this.desc = desc;
        this.color = color;
    }

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getColor() {
        return color;
    }
}
