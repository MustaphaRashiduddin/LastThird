package com.example.ibnshahid.lastthird;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ibnShahid on 25/10/2017.
 */

public class FetchAlarmData {
    public static ArrayList<ManualAlarmModel> allAlarms(Context context) {
        ArrayList<ManualAlarmModel> manualAlarmModelArrayList = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(context);
        Cursor res = db.getAlarms();
        if (res.getCount() == 0) {
            Log.e("db_error", "no results");
        } else {
            while (res.moveToNext()) { // I'm guessing this line moves one past -1 and then starts executing
                int pk = res.getInt(0);
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
        return manualAlarmModelArrayList;
    }

    public static ManualAlarmModel getAlarm(Context context, int id) {
        DatabaseHelper db = new DatabaseHelper(context);
        Cursor res = db.getAlarm(id);
        res.moveToNext();
        int pk = res.getInt(0);
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
        return new ManualAlarmModel(pk, hr, min, enabled, mon, tue, wed, thu, fri, sat, sun);
    }
}