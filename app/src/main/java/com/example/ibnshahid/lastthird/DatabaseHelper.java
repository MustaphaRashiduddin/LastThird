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

        db.execSQL("create table alarms(day integer not null, hr integer not null, min integer not null," +
                " alarm_group_id integer, primary key (day, hr, min), foreign key(alarm_group_id)" +
                " references alarm_groups(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists alarm_groups");
        db.execSQL("drop table if exists alarms");
        onCreate(db);
    }

    public void setAlarm(int day, int hr, int min, int fk) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("day", day);
        contentValues.put("hr", hr);
        contentValues.put("min", min);
        contentValues.put("alarm_group_id", fk);
        db.insert("alarms", null, contentValues);
        db.close();
    }

    public void deleteAlarm(int day, int hour, int min) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from alarms where (day = ? and hr = ? and min = ?)",
                new String[] {String.valueOf(day), String.valueOf(hour), String.valueOf(min)});
        db.close();
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
        db.close();
    }

    public void updateEnabled(int id, boolean enabled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("enabled", enabled);
        db.update("alarm_groups", cv, "id = "+id, null);
        db.close();
    }

    public void deleteAlarmGroup(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("alarm_groups", "id = ?", new String[] {String.valueOf(id)});
        db.execSQL("delete from alarms where alarm_group_id = ?", new String[] {String.valueOf(id)});
        db.close();
    }

    public boolean setAlarmGroup(int hr, int min, Boolean mon, Boolean tue, Boolean wed, Boolean thu,
                           Boolean fri, Boolean sat, Boolean sun, ManualAlarmGroupModel m) {
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

        final String MY_QUERY = "SELECT last_insert_rowid()";
        Cursor cur = db.rawQuery(MY_QUERY, null);
        cur.moveToFirst();
        int ID = cur.getInt(0);
        cur.close();
        db.close();
        m.pk = ID;

        if (result == -1) return false;
        else return true;
    }
}
