package cpit.prototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/01/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ImageDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE images ( " +
                "image_id INTEGER PRIMARY KEY, " +
                "image_name TEXT, "+
                "image_owner TEXT, "+
                "star_time TEXT, "+
                "end_time TEXT, "+
                "renewed_times INTEGER )";

        String CREATE_nig_TABLE = "CREATE TABLE image_stamps ( " +
                "id INTEGER PRIMARY KEY, " +
                "image_id INTEGER, "+
                "member_id INTEGER, "+
                "stamp TEXT, "+
                "duration REAL )";

        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_nig_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS images");
        this.onCreate(db);
    }

    public void addStamp(int img_id, int mem_id, String stp, double dur){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_id", img_id);
        values.put("member_id", mem_id);
        values.put("stamp", stp);
        values.put("duration", dur);
        db.insert("image_stamps", null, values);
        db.close();
    }

    public List<Image_stamp> getStamps() {
        List<Image_stamp> list = new ArrayList<Image_stamp>();
        String query = "SELECT  * FROM image_stamps";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Image_stamp tempStamp = new Image_stamp(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getFloat(4));
                list.add(tempStamp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void clearStamp(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("image_stamps", null, null);
        db.close();
    }

    public void addImage(Image img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image_id", img.id);
        values.put("image_name", img.image_name);
        values.put("image_owner", img.image_owner);
        values.put("star_time", img.star_time);
        values.put("end_time", img.end_time);
        values.put("renewed_times", img.renewed_times);
        db.insert("images", null, values);
        db.close();
    }

    public int findImage(String time) {
        String query = "Select * FROM images WHERE star_time <= '" + time + "' AND end_time > '" + time + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int shit;
        if (cursor.moveToFirst()) {
            shit = Integer.parseInt(cursor.getString(0));
        } else {
            shit = -1;
        }
        cursor.close();
        db.close();
        return shit;
    }

    public void clearImage(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("images", null, null);
        db.close();
    }

    public List<String> getAllImage() {
        List<String> list = new ArrayList<String>();
        String query = "SELECT  * FROM images";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Updating single book
  /*  public int updateBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title
        values.put("author", book.getAuthor()); // get author

        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(book.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_BOOKS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(book.getId()) });

        // 3. close
        db.close();

        Log.d("deleteBook", book.toString());

    }*/

}
