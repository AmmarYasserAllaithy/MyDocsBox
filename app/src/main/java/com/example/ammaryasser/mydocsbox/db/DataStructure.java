package com.example.ammaryasser.mydocsbox.db;

class Book {
    private int id;
    private String title, desc, color, create_ts, update_ts;

    Book(int id, String title, String desc, String color, String create_ts, String update_ts) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.color = color;
        this.create_ts = create_ts;
        this.update_ts = update_ts;
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

    public String getCreate_ts() {
        return create_ts;
    }

    public String getUpdate_ts() {
        return update_ts;
    }
}

class Doc {
    private int id, bookId;
    private String title, desc, tags, image, create_ts, update_ts;

    Doc(int id, int bookId, String title, String desc, String tags, String image, String create_ts, String update_ts) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.desc = desc;
        this.tags = tags;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public String getCreate_ts() {
        return create_ts;
    }

    public String getUpdate_ts() {
        return update_ts;
    }
}

class Tag {
    private int id;
    private String name, create_ts;

    Tag(int id, String name, String create_ts) {
        this.id = id;
        this.name = name;
        this.create_ts = create_ts;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreate_ts() {
        return create_ts;
    }
}

class User {
    private int id;
    private long Gid;
    private String firstName, lastName, email, password, gender, photo, create_TS;

    public User(int id, String firstName, String lastName, String email, String password, String gender, String photo, String create_TS) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.photo = photo;
        this.create_TS = create_TS;
    }

    public User(int id, long gid, String firstName, String lastName, String email, String password, String gender, String photo, String create_TS) {
        this.id = id;
        Gid = gid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.photo = photo;
        this.create_TS = create_TS;
    }

    public int getId() {
        return id;
    }

    public long getGid() {
        return Gid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoto() {
        return photo;
    }

    public String getCreate_TS() {
        return create_TS;
    }
}
