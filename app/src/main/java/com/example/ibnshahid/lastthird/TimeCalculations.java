package com.example.ibnshahid.lastthird;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by ibnShahid on 27/10/2017.
 */

public class TimeCalculations extends AppCompatActivity {
    protected Calendar fajrTime = Calendar.getInstance();
    private Calendar maghribTime = Calendar.getInstance();
    private Calendar calGetup = Calendar.getInstance();
    private SharedPreferences sp;

    private PendingIntent pendingIntent = null;
    private Intent intent = null;
    AlarmManager alarmManager = null;
    TimePickerDialog manual = null;
    private boolean alarmSet = false;

    private int calcDayOfMonth(int hourOfDay, int minute)
    {
        Calendar now = Calendar.getInstance();
        int hourNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minuteNow = Calendar.getInstance().get(Calendar.MINUTE);
        if (hourOfDay < hourNow) {
            now.add(Calendar.DAY_OF_MONTH, 1);
        } else if (hourOfDay == hourNow && minute < minuteNow) {
            now.add(Calendar.DAY_OF_MONTH, 1);
        }
        return now.get(Calendar.DAY_OF_MONTH);
    }

    long fajrInstance() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 6);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTimeInMillis();
    }

    long maghribInstance() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTimeInMillis();
    }

    void adjustdates() {
        maghribTime.set(Calendar.DATE ,Calendar.getInstance().get(Calendar.DATE));
        FajrTime.time.set(Calendar.DATE ,Calendar.getInstance().get(Calendar.DATE));

        int time_now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * 100 + Calendar.getInstance().get(Calendar.MINUTE);
        int fajr_time = FajrTime.time.get(Calendar.HOUR_OF_DAY) * 100 + FajrTime.time.get(Calendar.MINUTE);
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 0 || time_now > fajr_time) {
            FajrTime.getInstance().time.add(Calendar.DATE, 1);
        } else {
            maghribTime.add(Calendar.DATE, -1);
        }
        fajrTime = FajrTime.time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilities.getTime = Utilities.getTime12;

        sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        fajrTime.setTimeInMillis(sp.getLong("fajrMillis", fajrInstance()));
        Calendar tempMaghrib = Calendar.getInstance();
        tempMaghrib.setTimeInMillis(sp.getLong("maghribMillis", maghribInstance()));

        FajrTime.getInstance().time = Calendar.getInstance();
        FajrTime.getInstance().time.set(Calendar.HOUR_OF_DAY, fajrTime.get(Calendar.HOUR_OF_DAY));
        FajrTime.getInstance().time.set(Calendar.MINUTE, fajrTime.get(Calendar.MINUTE));

        maghribTime = Calendar.getInstance();
        maghribTime.set(Calendar.HOUR_OF_DAY, tempMaghrib.get(Calendar.HOUR_OF_DAY));
        maghribTime.set(Calendar.MINUTE, tempMaghrib.get(Calendar.MINUTE));

        // adjust dates for fajr and maghrib calendars
        adjustdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmSet = sp.getBoolean("alarmSet", false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("fajrCal", fajrTime);
        outState.putSerializable("maghribCal", maghribTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String fajr = savedInstanceState.getString("fajr");
        String maghrib = savedInstanceState.getString("maghrib");
        String lastThird = savedInstanceState.getString("last_third");
        fajrTime = (Calendar) savedInstanceState.getSerializable("fajrCal");
        maghribTime = (Calendar) savedInstanceState.getSerializable("maghribCal");
        timeMode = savedInstanceState.getString("timeMode");
    } private String timeMode;

    TimePickerDialog.OnTimeSetListener fajrOnTimeSetListener = (view, hourOfDay, minute) -> {
        fajrTime = FajrTime.time;
        fajrTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        fajrTime.set(Calendar.MINUTE, minute);
        adjustdates();
        FajrTime.getInstance().time = fajrTime;
    };

    TimePickerDialog.OnTimeSetListener maghribOnTimeSetListener = (view, hourOfDay, minute) -> {
        maghribTime = Calendar.getInstance();
        maghribTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        maghribTime.set(Calendar.MINUTE, minute);
    };

    void calcLastThird() {
        long end = FajrTime.time.getTimeInMillis();
        long begin = maghribTime.getTimeInMillis();
        long third = (end - begin)/3;
        long getup = FajrTime.time.getTimeInMillis() - third;
        calGetup = Calendar.getInstance();
        calGetup.setTimeInMillis(getup);
    }

    void cal_log(Calendar c) {
        Log.e("year", "" + c.get(Calendar.YEAR));
        Log.e("month", ""+c.get(Calendar.MONTH));
        Log.e("day of month", "" + c.get(Calendar.DAY_OF_MONTH));
        Log.e("time: ", "" + Utilities.getTime.fn(c));
    }
}
