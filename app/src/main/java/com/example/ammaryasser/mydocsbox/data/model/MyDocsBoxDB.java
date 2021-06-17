package com.example.ammaryasser.mydocsbox.data.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ammaryasser.mydocsbox.data.model.book.Book;
import com.example.ammaryasser.mydocsbox.data.model.book.BookDBDao;
import com.example.ammaryasser.mydocsbox.data.model.doc.Doc;
import com.example.ammaryasser.mydocsbox.data.model.doc.DocDBDao;
import com.example.ammaryasser.mydocsbox.data.model.tag.Tag;
import com.example.ammaryasser.mydocsbox.data.model.tag.TagDBDao;
import com.example.ammaryasser.mydocsbox.util.Converters;

import static com.example.ammaryasser.mydocsbox.util.Constant.DB_NAME;
import static com.example.ammaryasser.mydocsbox.util.Constant.DB_VERSION;


@Database(entities = {Book.class, Doc.class, Tag.class}, version = DB_VERSION, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MyDocsBoxDB extends RoomDatabase {

    private static volatile MyDocsBoxDB instance = null;

    public synchronized static MyDocsBoxDB getDatabase(Context context) {
        if (instance == null)
            instance = Room
                    .databaseBuilder(context, MyDocsBoxDB.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

        return instance;
    }

    public abstract BookDBDao bookDao();

    public abstract DocDBDao docDao();

    public abstract TagDBDao tagDao();

}
