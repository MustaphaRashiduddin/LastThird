package com.example.ibnshahid.lastthird;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        db.execSQL("create table alarm(id integer primary key, alarms_id integer, day integer)");
        // alarms_id is the foreign key
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists alarms");
        db.execSQL("drop table if exists alarm");
        onCreate(db);
    }

    public Boolean setAlarm(int id, int hr, int min, Boolean enabled, Boolean mon, Boolean tue, Boolean wed,
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
        setAlarm(db, id, sun, mon, tue, wed, thu, fri, sat);
        if (result == -1) return false;
        else return true;
    }

    void setAlarm(SQLiteDatabase db, int id, boolean... params) {
        for (int i=0; i<params.length; i++) {
            if (params[i]) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("alarms_id", id);
                contentValues.put("day", i+1);
                db.insert("alarm", null, contentValues);
            } else {

            }
        }
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
        setAlarm(db, id, sun, mon, tue, wed, thu, fri, sat);
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
