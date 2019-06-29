package com.example.ammaryasser.mydocsbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BooksDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "MyDocsBox.db",
            TABLE_BOOKS = "books",
            COL_ID = "ID",
            COL_TITLE = "TITLE",
            COL_DESCRIPTION = "DESCRIPTION",
            COL_COLOR = "COLOR",
            COL_CREATE_TS = "CREATE_TS",
            COL_UPDATE_TS = "UPDATE_TS";

    public BooksDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " VARCHAR(100), " +
                COL_DESCRIPTION + " TEXT, " +
                COL_COLOR + " TEXT, " +
                COL_CREATE_TS + " TEXT, " +
                COL_UPDATE_TS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String title, String desc, String color, String create_ts, String update_ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, desc);
        contentValues.put(COL_COLOR, color);
        contentValues.put(COL_CREATE_TS, create_ts);
        contentValues.put(COL_UPDATE_TS, update_ts);
        long result = getWritableDatabase().insert(TABLE_BOOKS, null, contentValues);
        return result != -1;
    }

    boolean update(int id, String title, String desc, String color, String create_ts, String update_ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, desc);
        contentValues.put(COL_COLOR, color);
        contentValues.put(COL_CREATE_TS, create_ts);
        contentValues.put(COL_UPDATE_TS, update_ts);
        int result = getWritableDatabase().update(TABLE_BOOKS, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    boolean delete(int id) {
        int result = getWritableDatabase().delete(TABLE_BOOKS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    boolean truncate() {
        return getWritableDatabase().delete(TABLE_BOOKS, "", new String[]{}) > 0;
    }

    ArrayList<Book> getAllBooks() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        ArrayList<Book> booksList = new ArrayList<>();
        if (result.getCount() > 0)
            while (result.moveToNext())
                booksList.add(new Book(
                        result.getInt(0),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6)));
        else return null;
        result.close();
        return booksList;
    }

}