package com.example.task91p.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.task91p.model.Advert;
import com.example.task91p.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {
            String CREATE_USER_TABLE =
                    "CREATE TABLE " + Util.TABLE_NAME
                            + "(" + Util.ADVERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                            + Util.TYPE + " TEXT,"
                            + Util.NAME + " TEXT,"
                            + Util.PHONE_NO + " TEXT,"
                            + Util.DESCRIPTION + " TEXT,"
                            + Util.DATE + " TEXT,"
                            + Util.LOCATION + " TEXT,"
                            + Util.LATITUDE + " DOUBLE,"
                            + Util.LONGITUDE + " DOUBLE)"
                    ;

            sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_USER_TABLE);

        onCreate(sqLiteDatabase);
    }

    //inserts an advert into the database
    public long insertAdvert(Advert advert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.TYPE, advert.get_type());
        contentValues.put(Util.NAME, advert.get_name());
        contentValues.put(Util.PHONE_NO, advert.get_phoneNo());
        contentValues.put(Util.DESCRIPTION, advert.get_description());
        contentValues.put(Util.DATE, advert.get_date());
        contentValues.put(Util.LOCATION, advert.get_location());
        contentValues.put(Util.LATITUDE, advert.get_latitude());
        contentValues.put(Util.LONGITUDE, advert.get_longitude());

        //attempt to insert the new row into table and return the id
        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }

    //removes an ad with selected id from the database
    public long deleteAdvert(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String args[] = {""+id};
        long deletedRowId = db.delete(Util.TABLE_NAME, Util.ADVERT_ID + "=?", args);
        return deletedRowId;
    }

    //selects user with given username and password
    public boolean fetchAdvert(String type, String name, String phoneNo, String description, String date, String location) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfRows;

        try {
            Cursor cursor =
                    db.query(Util.TABLE_NAME, new String[]{Util.ADVERT_ID},
                            Util.TYPE + "=? AND "
                                    + Util.NAME + "=? AND "
                                    + Util.PHONE_NO + "=? AND "
                                    + Util.DESCRIPTION + "=? AND "
                                    + Util.DATE + "=? AND "
                                    + Util.LOCATION + "=?",
                            new String[]{type, name, phoneNo, description, date, location}, null, null, null);
            numberOfRows = cursor.getCount();
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            numberOfRows = 0;
        }

        if (numberOfRows > 0) { return true; }
        else { return false; }
    }

    //returns a list of all adverts in database
    public List<Advert> fetchAllAdverts() {
        List<Advert> advertList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //define query in string format, then run that query
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAll, null);

        //starts from first row in database, each iteration cycles to next row until hitting last
        if (cursor.moveToFirst()) {
            do {
                Advert advert = new Advert();
                advert.set_advertID(cursor.getInt(0));
                advert.set_type(cursor.getString(1));
                advert.set_name(cursor.getString(2));
                advert.set_phoneNo(cursor.getString(3));
                advert.set_description(cursor.getString(4));
                advert.set_date(cursor.getString(5));
                advert.set_location(cursor.getString(6));
                advert.set_latitude(cursor.getDouble(7));
                advert.set_longitude(cursor.getDouble(8));

                advertList.add(advert);
            }while (cursor.moveToNext());
        }

        //filled our list, so return it
        return advertList;
    }
}