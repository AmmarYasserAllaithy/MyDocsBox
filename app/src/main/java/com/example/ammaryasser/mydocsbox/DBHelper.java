package com.example.ammaryasser.mydocsbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.ammaryasser.mydocsbox.data_structure.Book;
import com.example.ammaryasser.mydocsbox.data_structure.Doc;
import com.example.ammaryasser.mydocsbox.data_structure.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    static final String DB_NAME = "MyDocsBox.db";
    private final String DB_FILEPATH = getWritableDatabase().getPath();

    private static final String TABLE_BOOKS = "Books",
            COL_BOOK_ID = "ID",
            COL_BOOK_TITLE = "TITLE",
            COL_BOOK_DESCRIPTION = "DESCRIPTION",
            COL_BOOK_COLOR = "COLOR",
            COL_BOOK_CREATE_TS = "CREATE_TS",
            CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " (" +
                    COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_BOOK_TITLE + " VARCHAR(100), " +
                    COL_BOOK_DESCRIPTION + " TEXT, " +
                    COL_BOOK_COLOR + " TEXT, " +
                    COL_BOOK_CREATE_TS + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String TABLE_DOCS = "Docs",
            COL_DOC_ID = "ID",
            COL_DOC_BOOK_ID = "BOOK_ID",
            COL_DOC_TITLE = "TITLE",
            COL_DOC_DESC = "DESCRIPTION",
            COL_DOC_TAGS = "TAGS",
            COL_DOC_IMAGE = "IMAGE",
            COL_DOC_CREATE_TS = "CREATE_TS",
            COL_DOC_UPDATE_TS = "UPDATE_TS",
            CREATE_TABLE_DOCS = "CREATE TABLE " + TABLE_DOCS + " (" +
                    COL_DOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_DOC_BOOK_ID + " INTEGER, " +
                    COL_DOC_TITLE + " VARCHAR(100), " +
                    COL_DOC_DESC + " TEXT, " +
                    COL_DOC_TAGS + " TEXT, " +
                    COL_DOC_IMAGE + " BLOB, " +
                    COL_DOC_CREATE_TS + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COL_DOC_UPDATE_TS + " DATETIME)";

    private static final String TABLE_TAGS = "Tags",
            COL_TAG_ID = "ID",
            COL_TAG_Name = "NAME",
            COL_TAG_CREATE_TS = "CREATE_TS",
            CREATE_TABLE_TAGS = "CREATE TABLE " + TABLE_TAGS + " (" +
                    COL_TAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TAG_Name + " VARCHAR(100), " +
                    COL_TAG_CREATE_TS + " DATETIME DEFAULT CURRENT_TIMESTAMP)";


    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOKS);
        db.execSQL(CREATE_TABLE_DOCS);
        db.execSQL(CREATE_TABLE_TAGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
    }


    /**
     * TABLE BOOKs METHODS
     */
    boolean insertBook(String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOK_TITLE, title);
//        contentValues.put(COL_BOOK_DESCRIPTION, desc);
//        contentValues.put(COL_BOOK_COLOR, color);
        long result = getWritableDatabase().insert(TABLE_BOOKS, null, contentValues);
        return result != -1;
    }

    boolean updateBook(int id, String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOK_TITLE, title);
//        contentValues.put(COL_BOOK_DESCRIPTION, desc);
        long result = getWritableDatabase().update(
                TABLE_BOOKS,
                contentValues,
                COL_BOOK_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        return result > 0;
    }

    boolean deleteBook(int id) {
        int result = getWritableDatabase().delete(TABLE_BOOKS, COL_BOOK_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    Book selectBook(int id) {
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_BOOKS + " WHERE ID = ?",
                new String[]{String.valueOf(id)}
        );
        if (result.getCount() > 0 && result.moveToNext()) return new Book(
                result.getInt(0),
                result.getString(1),
                result.getString(2),
                result.getString(3)
        );
        else result.close();
        return null;
    }

    ArrayList<Book> getAllBooks() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        ArrayList<Book> booksList = new ArrayList<>();
        if (result.getCount() > 0)
            while (result.moveToNext())
                booksList.add(new Book(
                        result.getInt(0),
                        result.getString(1),
                        result.getString(2),
                        result.getString(3)));
        else return null;
        result.close();
        return booksList;
    }

    Book getLastBook() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
        if (result.getCount() > 0)
            if (result.moveToLast())
                return new Book(
                        result.getInt(0),
                        result.getString(1),
                        result.getString(1),
                        result.getString(1));
        result.close();
        return null;
    }

    String getBookTitleOf(int bookId) {
        ArrayList<Book> books = getAllBooks();
        for (Book book : books) if (book.getId() == bookId) return book.getTitle();
        return null;
    }

    int getBooksNo() {
        return getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_BOOKS, null).getCount();
    }


    /**
     * TABLE DOCs METHODS
     */
    boolean insertDoc(int bookId, String title, String desc, String tagsIdx, byte[] imageBytes) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DOC_BOOK_ID, bookId);
        contentValues.put(COL_DOC_TITLE, title);
        contentValues.put(COL_DOC_DESC, desc);
        contentValues.put(COL_DOC_TAGS, tagsIdx);
        contentValues.put(COL_DOC_IMAGE, imageBytes);
        long result = getWritableDatabase().insert(TABLE_DOCS, null, contentValues);
        return result != -1;
    }

    boolean updateDoc(int id, int bookId, String title, String desc, String tagsIdx, byte[] imageBytes) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DOC_BOOK_ID, bookId);
        contentValues.put(COL_DOC_TITLE, title);
        contentValues.put(COL_DOC_DESC, desc);
        contentValues.put(COL_DOC_TAGS, tagsIdx);
        contentValues.put(COL_DOC_IMAGE, imageBytes);
        contentValues.put(COL_DOC_UPDATE_TS, MainActivity.getDateTime());
        long result = getWritableDatabase().update(
                TABLE_DOCS,
                contentValues,
                COL_DOC_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        return result > 0;
    }

    boolean deleteDoc(int id) {
        int result = getWritableDatabase().delete(TABLE_DOCS, COL_DOC_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    Doc selectDoc(int id) {
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_DOCS + " WHERE ID = ?", new String[]{String.valueOf(id)});
        if (result.getCount() > 0 && result.moveToNext())
            return new Doc(
                    result.getInt(0),
                    result.getInt(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getBlob(5),
                    result.getString(6),
                    result.getString(7));
        else result.close();
        return null;
    }

    ArrayList<Doc> getAllDocs() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_DOCS, null);
        if (result.getCount() > 0) return cursorToListDocs(result);
        else return null;
    }

    ArrayList<Doc> getDocsInBook(int bookID) {
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_DOCS + " WHERE " + COL_DOC_BOOK_ID + " = ?",
                new String[]{String.valueOf(bookID)}
        );

        if (result.getCount() > 0) return cursorToListDocs(result);
        else return null;
    }

    ArrayList<Doc> getDocsWithTag(int tagID) {
        //ToDo: test query later
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_DOCS + " WHERE " + COL_DOC_TAGS + " LIKE ?",
                new String[]{" " + tagID + ", "}
        );

        if (result.getCount() > 0) return cursorToListDocs(result);
        else return null;
    }

    int getDocsNo() {
        return getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_DOCS, null).getCount();
    }


    /**
     * TABLE TAGs METHODS
     */
    boolean insertTag(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TAG_Name, name);
        long result = this.getWritableDatabase().insert(TABLE_TAGS, null, contentValues);
        return result != -1;
    }

    boolean updateTag(int id, String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TAG_Name, name);
        long result = getWritableDatabase().update(
                TABLE_TAGS,
                contentValues,
                COL_TAG_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        return result > 0;
    }

    boolean deleteTag(int id) {
        int result = getWritableDatabase().delete(TABLE_TAGS, COL_TAG_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    Tag selectTag(String id) {
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_TAGS + " WHERE " + COL_TAG_ID + " = ?",
                new String[]{id}
        );
        if (result.getCount() > 0 && result.moveToNext())
            return new Tag(result.getInt(0), result.getString(1));
        else result.close();
        return null;
    }

    Tag selectTag(int id) {
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_TAGS + " WHERE " + COL_TAG_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
        if (result.getCount() > 0 && result.moveToNext())
            return new Tag(result.getInt(0), result.getString(1));
        else result.close();
        return null;
    }

    ArrayList<Tag> getAllTags() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS, null);

        if (result.getCount() > 0) return cursorToListTags(result);
        else return null;
    }

    Tag getLastTag() {
        Cursor result = getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS, null);
        if (result.getCount() > 0)
            if (result.moveToLast())
                return new Tag(result.getInt(0), result.getString(1));
        result.close();
        return null;
    }

    int getTagsNo() {
        return getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_TAGS, null).getCount();
    }


    /**
     * Search
     */
    ArrayList<Doc> search(String target) {
        Cursor result = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_DOCS
                        + " WHERE " + COL_DOC_TITLE + " LIKE ?" +
                        " OR " + COL_DOC_DESC + " LIKE ?",
                new String[]{'%' + target + '%', '%' + target + '%'}
        );

        if (result.getCount() > 0) return cursorToListDocs(result);
        else return null;
    }


    /**
     * Utility
     *
     * @param cursor : DB query result
     * @return list of Docs, Books, Tags
     */
    private ArrayList<Doc> cursorToListDocs(Cursor cursor) {
        ArrayList<Doc> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(new Doc(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getBlob(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));
        cursor.close();
        return list;
    }

    private ArrayList<Book> cursorToListBooks(Cursor cursor) {
        ArrayList<Book> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(new Book(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        cursor.close();
        return list;
    }

    private ArrayList<Tag> cursorToListTags(Cursor cursor) {
        ArrayList<Tag> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(new Tag(cursor.getInt(0), cursor.getString(1)));
        cursor.close();
        return list;
    }


    /**
     * Export & import DB
     * <p>
     * Copies the database file at the specified location over the current internal application database.
     */
    String exportDatabase() {
        File backupDB = new File(Environment.getExternalStorageDirectory() + File.separator + getDatabaseName());
        exportDatabase(backupDB);
        return backupDB.getAbsolutePath();
    }

    private void exportDatabase(File backupDB) {
        // Close the SQLiteOpenHelper so it will commit the created empty database to internal storage.
        close();
        try {
            File currentDB = new File(DB_FILEPATH);
            copyFile(new FileInputStream(currentDB), new FileOutputStream(backupDB));
            // Access the copied database so SQLiteHelper will cache it and mark it as created.
            getWritableDatabase().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void backup() {
        File backupDB = new File(Environment.getExternalStorageDirectory() + File.separator + "backup - " + getDatabaseName());
        exportDatabase(backupDB);
    }

    boolean importDatabase(String DBPath) {
        // Close the SQLiteOpenHelper so it will commit the created empty database to internal storage.
        close();
        try {
            File backupDB = new File(DBPath);
            File currentDB = new File(DB_FILEPATH);
            if (backupDB.exists()) {
                copyFile(new FileInputStream(backupDB), new FileOutputStream(currentDB));
                // Access the copied database so SQLiteHelper will cache it and mark it as created.
                getWritableDatabase().close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Creates the specified <code>toFile</code> as a byte for byte copy of the
     * <code>fromFile</code>. If <code>toFile</code> already exists, then it
     * will be replaced with a copy of <code>fromFile</code>. The name and path
     * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
     * <br/>
     * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
     * this function.</i>
     *
     * @param fromFile - FileInputStream for the file to copy from.
     * @param toFile   - FileInputStream for the file to copy to.
     */
    private static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) fromChannel.close();
            } finally {
                if (toChannel != null) toChannel.close();
            }
        }
    }

}