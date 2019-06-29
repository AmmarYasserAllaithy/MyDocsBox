package com.example.ammaryasser.mydocsbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TagsDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "MyDocsBox.db",
            TABLE_TAGS = "tags",
            COL_ID = "ID",
            COL_Name = "NAME",
            COL_CREATE_TS = "CREATE_TS";

    public TagsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TAGS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_Name + " VARCHAR(100), " +
                COL_CREATE_TS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String name, String create_ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_Name, name);
        contentValues.put(COL_CREATE_TS, create_ts);
        long result = getWritableDatabase().insert(TABLE_TAGS, null, contentValues);
        return result != -1;
    }

    boolean delete(int id) {
        int result = getWritableDatabase().delete(TABLE_TAGS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    ArrayList<Tag> getAllTags() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS, null);
        ArrayList<Tag> tagsList = new ArrayList<>();
        if (result.getCount() > 0)
            while (result.moveToNext())
                tagsList.add(new Tag(
                        result.getInt(0),
                        result.getString(1),
                        result.getString(2)));
        else return null;
        result.close();
        return tagsList;
    }

}
