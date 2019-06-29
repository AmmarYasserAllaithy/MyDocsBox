package com.example.ammaryasser.mydocsbox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DocsDBHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "MyDocsBox.db",
            TABLE_DOCS = "docs",
            COL_ID = "ID",
            COL_BOOK_ID = "BOOK_ID",
            COL_TITLE = "TITLE",
            COL_DESCRIPTION = "DESCRIPTION",
            COL_TAGS = "TAGS",
            COL_IMAGE = "IMAGE",
            COL_CREATE_TS = "CREATE_TS",
            COL_UPDATE_TS = "UPDATE_TS";

    public DocsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DOCS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOK_ID + " INTEGER, " +
                COL_TITLE + " VARCHAR(100), " +
                COL_DESCRIPTION + " TEXT, " +
                COL_TAGS + " TEXT, " +
                COL_IMAGE + " BLOB, " +
                COL_CREATE_TS + " TEXT, " +
                COL_UPDATE_TS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCS);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(int bookId, String title, String desc, String tags, String image, String create_ts, String update_ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOK_ID, bookId);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, desc);
        contentValues.put(COL_TAGS, tags);
        contentValues.put(COL_IMAGE, image);
        contentValues.put(COL_CREATE_TS, create_ts);
        contentValues.put(COL_UPDATE_TS, update_ts);
        long result = getWritableDatabase().insert(TABLE_DOCS, null, contentValues);
        return result != -1;
    }

    boolean update(int id, int bookId, String title, String desc, String tags, String image, String create_ts, String update_ts) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOK_ID, bookId);
        contentValues.put(COL_TITLE, title);
        contentValues.put(COL_DESCRIPTION, desc);
        contentValues.put(COL_TAGS, tags);
        contentValues.put(COL_IMAGE, image);
        contentValues.put(COL_CREATE_TS, create_ts);
        contentValues.put(COL_UPDATE_TS, update_ts);
        int result = getWritableDatabase().update(TABLE_DOCS, contentValues, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    boolean delete(int id) {
        int result = getWritableDatabase().delete(TABLE_DOCS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    boolean truncate() {
        return getWritableDatabase().delete(TABLE_DOCS, "", new String[]{}) > 0;
    }

    ArrayList<Doc> getAllDocs() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_DOCS, null);
        ArrayList<Doc> docsList = new ArrayList<>();
        if (result.getCount() > 0)
            while (result.moveToNext())
                docsList.add(new Doc(
                        result.getInt(0),
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3),
                        result.getString(4),
                        result.getString(5),
                        result.getString(6),
                        result.getString(7)));
        else return null;
        result.close();
        return docsList;
    }

    public void insertImage(View view) throws IOException {
        FileInputStream fis = new FileInputStream("/storage/sdcard/demoImage.jpg");
        byte[] image = new byte[fis.available()];
        fis.read(image);
        ContentValues values = new ContentValues();
        values.put("a", image);
        fis.close();
        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
    }

    public void getImage(View view) {
        Cursor c = getWritableDatabase().rawQuery("select * from imageTb", null);
        if (c.moveToNext()) {
            byte[] image = c.getBlob(0);
            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
//            imageView.setImageBitmap(bmp);
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }
    }
}
