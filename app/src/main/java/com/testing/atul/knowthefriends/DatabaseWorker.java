package com.testing.atul.knowthefriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Atul on 6/28/2016.
 */
public class DatabaseWorker extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "Contacts";

    public DatabaseWorker(Context context){
        super(context, "ContactsDB.db" , null, 1);
    }

    String[] names = {"Alpha", "Beta", "Cupcake", "Donut", "Eclair"};
    String[] nums = {"123", "234", "345", "456", "567"};
    Integer[] pics = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,};

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS Contacts (" +
                        "Sno INTEGER PRIMARY KEY, " +
                        "Name CHAR(10) ," +
                        "Number CHAR(10) );"
        );
        for(int i=0; i<names.length; i++) {
            ContentValues vals = new ContentValues();
            vals.put("Sno", (i+1));
            vals.put("Name", names[i]);
            vals.put("Number", nums[i]);
            /*
            b= BitmapFactory.decodeResource(Resources.getSystem(), pics[i]);
            bos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, bos);
            vals.put("Icon", bos.toByteArray()); */

            db.insert(TABLE_NAME, null, vals);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contacts");
        onCreate(db);
    }


    public boolean insertRec(String name, String num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();

        vals.put("Name", name);
        vals.put("Number", num);

        db.insert(TABLE_NAME, null, vals);
        db.close();
        return true;
    }

    public int numberOfRows() {
       SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public void updateRec(int sno, String name, String num) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Sno", sno);
            contentValues.put("Name", name);
            contentValues.put("Number", num);
            db.update(TABLE_NAME, contentValues, "Sno = ?", new String[]{String.valueOf(sno)});
            db.close();
        }catch(SQLException e)
        {
            Log.e("TAG", "Message");
        }
    }

    public Cursor getPerson(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Contacts WHERE Name = ?", new String[]{name});
    }

    public int getSno(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "Sno",
                        "Name", "Number" }, "Name =?",
                new String[] { name }, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();
        return Integer.parseInt(cursor.getString(0));
    }

}
