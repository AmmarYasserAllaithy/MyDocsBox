package com.example.ammaryasser.mydocsbox;

public class DBStructure {

    public static class Book {

        private int id;
        private String title, desc, color;

        public Book(int id, String title, String desc, String color) {
            this.id = id;
            this.title = title;
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

    public static class Doc {

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

    public static class Tag {

        private int id;
        private String name;

        public Tag(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

//    class User {
//        private int id;
//        private long Gid;
//        private String firstName, lastName, email, password, gender, photo, create_TS;
//
//        public User(int id, String firstName, String lastName, String email, String password, String gender, String photo, String create_TS) {
//            this.id = id;
//            this.firstName = firstName;
//            this.lastName = lastName;
//            this.email = email;
//            this.password = password;
//            this.gender = gender;
//            this.photo = photo;
//            this.create_TS = create_TS;
//        }
//
//        public User(int id, long gid, String firstName, String lastName, String email, String password, String gender, String photo, String create_TS) {
//            this.id = id;
//            Gid = gid;
//            this.firstName = firstName;
//            this.lastName = lastName;
//            this.email = email;
//            this.password = password;
//            this.gender = gender;
//            this.photo = photo;
//            this.create_TS = create_TS;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public long getGid() {
//            return Gid;
//        }
//
//        public String getFirstName() {
//            return firstName;
//        }
//
//        public String getLastName() {
//            return lastName;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//
//        public String getGender() {
//            return gender;
//        }
//
//        public String getPhoto() {
//            return photo;
//        }
//
//        public String getCreate_TS() {
//            return create_TS;
//        }
//    }
}
