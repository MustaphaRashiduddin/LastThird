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
        db.execSQL("create table alarm_groups(id integer primary key, hr integer, min integer, enabled integer, " +
                "mon integer, tue integer, wed integer, thu integer, fri integer, sat integer, sun integer)");

        db.execSQL("create table alarms(day integer not null, hr integer not null, min integer not null, primary key (day, hr, min))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists alarm_groups");
        db.execSQL("drop table if exists alarms");
        onCreate(db);
    }

    public Boolean setAlarmGroup(int id, int hr, int min, Boolean enabled, Boolean mon, Boolean tue, Boolean wed,
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
        long result = db.insert("alarm_groups", null, contentValues);
        setAlarmGroup(db, id, sun, mon, tue, wed, thu, fri, sat);
        if (result == -1) return false;
        else return true;
    }

    public void setAlarms(int day, int hr, int min) {
//        insert into alarms (day, hr, min) values (1,5,53);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("hr", hr);
        contentValues.put("min", min);
        db.insert("alarms", null, contentValues);
    }

    void setAlarmGroup(SQLiteDatabase db, int id, boolean... params) {
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
        return db.rawQuery("select * from alarm_groups", null);
    }

    public Cursor getAlarm(int n) {
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("select * from alarm_groups where id = ?", new String[] {String.valueOf(n)});
    }

    public void updateAlarmGroup(int id, int hr, int min, boolean enabled, boolean mon, boolean tue,
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
        db.update("alarm_groups", cv, "id = "+id, null);
        setAlarmGroup(db, id, sun, mon, tue, wed, thu, fri, sat);
    }

    public void updateEnabled(int id, boolean enabled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("enabled", enabled);
        db.update("alarm_groups", cv, "id = "+id, null);
    }

    public void deleteAlarm(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("alarms_groups", "id = ?", new String[] {String.valueOf(id)});
    }

    public void deleteDayAlarm(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("day_alarm", "id = ?", new String[] {String.valueOf(id)});
    }
}
