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
                " alarm_group_id integer, primary key (day, hr, min, alarm_group_id), foreign key(alarm_group_id)" +
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

    public void deleteAlarm(int day, int hour, int min, int fk) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from alarms where (day = ? and hr = ? and min = ? and alarm_group_id = ?)",
                new String[] {String.valueOf(day), String.valueOf(hour), String.valueOf(min), String.valueOf(fk)});
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

    public void updateAlarmGroup(ManualAlarmGroupModel m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hr", m.hr);
        cv.put("min", m.min);
        cv.put("enabled", m.enabled);
        cv.put("mon", m.mon);
        cv.put("tue", m.tue);
        cv.put("wed", m.wed);
        cv.put("thu", m.thu);
        cv.put("fri", m.fri);
        cv.put("sat", m.sat);
        cv.put("sun", m.sun);
        db.update("alarm_groups", cv, "id = "+m.pk, null);
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

    public boolean setAlarmGroup(ManualAlarmGroupModel m) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("hr", m.hr);
        contentValues.put("min", m.min);
        contentValues.put("enabled", 1);
        contentValues.put("mon", m.mon);
        contentValues.put("tue", m.tue);
        contentValues.put("wed", m.wed);
        contentValues.put("thu", m.thu);
        contentValues.put("fri", m.fri);
        contentValues.put("sat", m.sat);
        contentValues.put("sun", m.sun);
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
