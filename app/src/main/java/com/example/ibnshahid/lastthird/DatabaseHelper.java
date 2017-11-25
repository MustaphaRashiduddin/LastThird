package com.example.ibnshahid.lastthird;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ibnShahid on 17/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "alarms.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table alarms(id integer primary key, hr integer, min integer, enabled integer, " +
                "mon integer, tue integer, wed integer, thu integer, fri integer, sat integer, sun integer)");
        Log.e("DatabaseHelper", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists alarms");
        onCreate(db);
        Log.e("DatabaseHelper", "onUpgrade");
    }

    public Boolean setAlarm(int hr, int min) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("insert into alarms(time, enabled, mon, tue, wed, thu, fri, sat, sun)" +
//                " values(hr+':'+min, 1, 0, 0, 0, 0, 0, 0, 0)");
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

    public Boolean UpdateTimeFormat() {
        ArrayList<ManualAlarmModel> manualAlarmModelArrayList = new ArrayList<>();
        Cursor res = getAlarms();
        if (res.getCount() == 0) {
//            Toast.makeText(context, "no results", Toast.LENGTH_SHORT).show();
        } else {
            while (res.moveToNext()) { // I'm guessing this line moves one past -1 and then starts executing
                int pk = res.getInt(0);
//                String time = res.getString(1);
                int hr = res.getInt(1);
                int min = res.getInt(2);
                int enabled = res.getInt(3);
                int mon = res.getInt(4);
                int tue = res.getInt(5);
                int wed = res.getInt(6);
                int thu = res.getInt(7);
                int fri = res.getInt(8);
                int sat = res.getInt(9);
                int sun = res.getInt(10);
                ManualAlarmModel manualAlarmModel = new ManualAlarmModel(pk, hr, min, enabled,
                        mon, tue, wed, thu, fri, sat, sun);
                manualAlarmModelArrayList.add(manualAlarmModel);
            }
        }

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i=0; i<manualAlarmModelArrayList.size(); i++) {
//            ContentValues
        }

        return true;
    }

    public Cursor getAlarms() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from alarms", null);
    }
}
