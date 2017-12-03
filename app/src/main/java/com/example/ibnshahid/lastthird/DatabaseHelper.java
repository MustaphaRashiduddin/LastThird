package com.example.ibnshahid.lastthird;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ibnShahid on 17/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "alarms.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table alarms(id integer primary key, hr integer, min integer, enabled integer, " +
                "mon integer, tue integer, wed integer, thu integer, fri integer, sat integer, sun integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists alarms");
        onCreate(db);
    }

    public Boolean setAlarm(int hr, int min) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hr", hr);
        contentValues.put("min", min);
        contentValues.put("enabled", 1);
        contentValues.put("mon", 0);
        contentValues.put("tue", 0);
        contentValues.put("wed", 0);
        contentValues.put("thu", 0);
        contentValues.put("fri", 0);
        contentValues.put("sat", 0);
        contentValues.put("sun", 0);
        long result = db.insert("alarms", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public Boolean setAlarm(int hr, int min, Boolean enabled, Boolean mon, Boolean tue, Boolean wed,
                            Boolean thu, Boolean fri, Boolean sat, Boolean sun) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hr", hr);
        contentValues.put("min", min);
        contentValues.put("enabled", 1);
        contentValues.put("mon", mon);
        contentValues.put("tue", tue);
        contentValues.put("wed", wed);
        contentValues.put("thu", thu);
        contentValues.put("fri", fri);
        contentValues.put("sat", sat);
        contentValues.put("sun", sun);
        long result = db.insert("alarms", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public Cursor getAlarms() {
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from alarms", null);
    }

    public Cursor getAlarm(int n) {
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from alarms where id = ?", new String[] {String.valueOf(n)});
    }

    public void updateAlarm(int id, int hr, int min, boolean enabled, boolean mon, boolean tue,
                            boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hr", hr);
        cv.put("min", min);
        cv.put("enabled", enabled);
        cv.put("mon", mon);
        cv.put("tue", tue);
        cv.put("wed", wed);
        cv.put("thu", thu);
        cv.put("fri", fri);
        cv.put("sat", sat);
        cv.put("sun", sun);
        db.update("alarms", cv, "id = "+id, null);
    }

    public void updateEnabled(int id, boolean enabled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("enabled", enabled);
        db.update("alarms", cv, "id = "+id, null);
    }

    public void deleteAlarm(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("alarms", "id = ?", new String[] {String.valueOf(id)});
    }
}
