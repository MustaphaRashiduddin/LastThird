package com.example.ibnshahid.lastthird;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    private TextView fajrDisplay = null;
    private TextView maghribDisplay = null;
    private TextView tvLastThird = null;
    private Calendar fajrTime = Calendar.getInstance();
    private Calendar maghribTime = Calendar.getInstance();
    private Calendar calGetup = Calendar.getInstance();
    private MenuItem miTimeMode = null;

    private PendingIntent pendingIntent = null;
    private Intent intent = null;
    AlarmManager alarmManager = null;
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog manual = null;

    public boolean alarmSet = false;
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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("fajrMillis", fajrTime.getTimeInMillis());
        editor.putLong("maghribMillis", maghribTime.getTimeInMillis());
        editor.commit();
    }

    // when manually setting the time within the constraints of the last third of the night and isha time
    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        Calendar getup = Calendar.getInstance();
        getup.set(Calendar.YEAR, FajrTime.time.get(Calendar.YEAR));
        int dayOfMonth = calcDayOfMonth(hourOfDay, minute);
        getup.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        getup.set(Calendar.HOUR_OF_DAY, hourOfDay);
        getup.set(Calendar.MINUTE, minute);
        Utilities.setAlarm(this, getup);
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

    SharedPreferences sp;

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

        fajrDisplay = (TextView) findViewById(R.id.tv_show_fajr_time);
        Button fajrDisplayTPDButton = (Button) findViewById(R.id.btn_pic_fajr_time);
        fajrDisplayTPDButton.setOnClickListener(v -> new TimePickerDialog(MainActivity.this, fajrOnTimeSetListener, fajrTime.get(Calendar.HOUR_OF_DAY),
                fajrTime.get(Calendar.MINUTE), Utilities.getTime.equals(Utilities.getTime24)).show());
        fajrDisplay.setText(Utilities.getTime.fn(FajrTime.getInstance().time));

        maghribDisplay = (TextView) findViewById(R.id.tv_show_maghrib_time);
        Button maghribDisplayTPDButton = (Button) findViewById(R.id.btn_pic_maghrib_time);
        maghribDisplayTPDButton.setOnClickListener(v -> new TimePickerDialog(MainActivity.this, maghribOnTimeSetListener, maghribTime.get(Calendar.HOUR_OF_DAY),
                maghribTime.get(Calendar.MINUTE), Utilities.getTime.equals(Utilities.getTime24)).show());
        maghribDisplay.setText(Utilities.getTime.fn(maghribTime));

        tvLastThird = (TextView) findViewById(R.id.tv_last_third);
        tvAlarmUnset();

        // get alarm status
        if (alarmSet) {
            tvLastThird.setText("Alarm set for " + Utilities.getTime.fn(calGetup));
        } else {
            tvLastThird.setText("Alarm not set");
        }

        Button btnAlarm = (Button) findViewById(R.id.btn_set_alarm);
        btnAlarm.setOnClickListener(v -> {
            calcLastThird();
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alarm");
            alertDialog.setMessage("Do you want the system to set the time or yourself manually?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "AUTOMATIC",
                    (dialog, which) -> {
                        Utilities.setAlarm(this, calGetup);
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "MANUAL",
                    (dialog, which) -> {
                        manual = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(this,
                                        calGetup.get(Calendar.HOUR_OF_DAY), calGetup.get(Calendar.MINUTE),
                                        Utilities.getTime.equals(Utilities.getTime24));
//                        manual.setMinTime(calGetup.get(Calendar.HOUR_OF_DAY), calGetup.get(Calendar.MINUTE), 0);
//                        manual.setMaxTime(fajrTime.get(Calendar.HOUR_OF_DAY), fajrTime.get(Calendar.MINUTE), 0);
                        manual.show(getFragmentManager(), "Timepickerdialog");
                        manual.dismissOnPause(true);
                    });
            alertDialog.show();
        });

        Button btnCancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        btnCancelAlarm.setOnClickListener(v -> {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(2);

            // persisting notification state
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("alarmSet", false);
            editor.commit();

            Intent intent = new Intent(this, AlarmService.class);
            stopService(intent);

            intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.cancel(pendingIntent);

            tvAlarmUnset();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmSet = sp.getBoolean("alarmSet", false);
        if (alarmSet) {
            tvLastThird.setText("Alarm set for " + Utilities.getTime.fn(calGetup));
        } else {
            tvLastThird.setText("Alarm not set");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        miTimeMode = menu.findItem(R.id.time_mode);
        if (timeMode != null) miTimeMode.setTitle(timeMode);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time_mode:
                if (miTimeMode.getTitle().toString().equals(getString(R.string.time_mode_24))) {
                    Utilities.getTime = Utilities.getTime24;
                    miTimeMode.setTitle(R.string.time_mode_pm);
                } else {
                    Utilities.getTime = Utilities.getTime12;
                    miTimeMode.setTitle(R.string.time_mode_24);
                }
                setAllTimeTextViews();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fajr", fajrDisplay.getText().toString());
        outState.putString("maghrib", maghribDisplay.getText().toString());
        outState.putString("last_third", tvLastThird.getText().toString());
        outState.putSerializable("fajrCal", fajrTime);
        outState.putSerializable("maghribCal", maghribTime);
        outState.putString("timeMode", miTimeMode.getTitle().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String fajr = savedInstanceState.getString("fajr");
        String maghrib = savedInstanceState.getString("maghrib");
        String lastThird = savedInstanceState.getString("last_third");
        fajrDisplay.setText(fajr);
        maghribDisplay.setText(maghrib);
        tvLastThird.setText(lastThird);
        fajrTime = (Calendar) savedInstanceState.getSerializable("fajrCal");
        maghribTime = (Calendar) savedInstanceState.getSerializable("maghribCal");
        timeMode = savedInstanceState.getString("timeMode");
    } private String timeMode;

    TimePickerDialog.OnTimeSetListener fajrOnTimeSetListener = (view, hourOfDay, minute) -> {
        fajrTime = FajrTime.time;
        fajrTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        fajrTime.set(Calendar.MINUTE, minute);
        fajrDisplay.setText(Utilities.getTime.fn(fajrTime));
        adjustdates();
        FajrTime.getInstance().time = fajrTime;
    };

    TimePickerDialog.OnTimeSetListener maghribOnTimeSetListener = (view, hourOfDay, minute) -> {
        maghribTime = Calendar.getInstance();
        maghribTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        maghribTime.set(Calendar.MINUTE, minute);
        maghribDisplay.setText(Utilities.getTime.fn(maghribTime));
    };

    void setAllTimeTextViews() {
        fajrDisplay.setText(Utilities.getTime.fn(fajrTime));
        maghribDisplay.setText(Utilities.getTime.fn(maghribTime));
        tvLastThird.setText("Alarm set for " + Utilities.getTime.fn(calGetup));
        calcLastThird();
    }

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

    void tvAlarmUnset() {
        tvLastThird.setText("Alarm not set");
    }
}
