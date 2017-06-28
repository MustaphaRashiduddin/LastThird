package com.example.ibnshahid.lastthird;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("fajrMillis", fajrTime.getTimeInMillis());
        editor.putLong("maghribMillis", maghribTime.getTimeInMillis());
        editor.commit();
    }

    //    when manually setting the time within the constraints of the last third of the night and isha time
    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        intent = new Intent(MainActivity.this, AlarmReceiver.class);
        // TODO fix time
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calGetup.getTimeInMillis(), pendingIntent);
        Calendar getup = Calendar.getInstance();
        getup.setTimeInMillis(calGetup.getTimeInMillis());
        getup.add(Calendar.HOUR_OF_DAY, hourOfDay - calGetup.get(Calendar.HOUR_OF_DAY));
        getup.add(Calendar.MINUTE, minute - calGetup.get(Calendar.MINUTE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(0, getup.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                    getup.getTimeInMillis(), pendingIntent);
        }
        createNotification(getApplicationContext(), getup);
    }

    interface GetTimeInterface {
        String fn(Calendar cal);
    }

    GetTimeInterface getTime = null;

    long fajrInstance() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTime = getTime12;

        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        fajrTime.setTimeInMillis(sp.getLong("fajrMillis", fajrInstance()));
        Calendar tempMaghrib = Calendar.getInstance();
        tempMaghrib.setTimeInMillis(sp.getLong("maghribMillis", maghribInstance()));

        // get alarm status
        alarmSet = sp.getBoolean("alarmSet", false);
        if (alarmSet)
            Toast.makeText(this, "alarm is set", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "alarm not set", Toast.LENGTH_SHORT).show();

        FajrTime.getInstance().time = Calendar.getInstance();
        FajrTime.getInstance().time.add(Calendar.DATE, 1);
        FajrTime.getInstance().time.set(Calendar.HOUR_OF_DAY, fajrTime.get(Calendar.HOUR_OF_DAY));
        FajrTime.getInstance().time.set(Calendar.MINUTE, fajrTime.get(Calendar.MINUTE));

        maghribTime = Calendar.getInstance();
        maghribTime.set(Calendar.HOUR_OF_DAY, tempMaghrib.get(Calendar.HOUR_OF_DAY));
        maghribTime.set(Calendar.MINUTE, tempMaghrib.get(Calendar.MINUTE));

        fajrDisplay = (TextView) findViewById(R.id.tv_show_fajr_time);
        Button fajrDisplayTPDButton = (Button) findViewById(R.id.btn_pic_fajr_time);
        fajrDisplayTPDButton.setOnClickListener(v -> new TimePickerDialog(MainActivity.this, fajrOnTimeSetListener, fajrTime.get(Calendar.HOUR_OF_DAY),
                fajrTime.get(Calendar.MINUTE), getTime.equals(getTime24)).show());
        fajrDisplay.setText(getTime.fn(FajrTime.getInstance().time));

        maghribDisplay = (TextView) findViewById(R.id.tv_show_maghrib_time);
        Button maghribDisplayTPDButton = (Button) findViewById(R.id.btn_pic_maghrib_time);
        maghribDisplayTPDButton.setOnClickListener(v -> new TimePickerDialog(MainActivity.this, maghribOnTimeSetListener, maghribTime.get(Calendar.HOUR_OF_DAY),
                maghribTime.get(Calendar.MINUTE), getTime.equals(getTime24)).show());
        maghribDisplay.setText(getTime.fn(maghribTime));

        tvLastThird = (TextView) findViewById(R.id.tv_last_third);
        tvAlarmUnset();

        Button btnAlarm = (Button) findViewById(R.id.btn_set_alarm);
        btnAlarm.setOnClickListener(v -> {
            calcLastThird();
            if (calGetup.get(Calendar.HOUR_OF_DAY) > 5) {
                Toast.makeText(this, "maghrib and fajr time configurations not possible", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "" + getTime.fn(calGetup), Toast.LENGTH_SHORT).show();
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Alarm");
                alertDialog.setMessage("Do you want the system to set the time or yourself manually?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "AUTOMATIC",
                        (dialog, which) -> {
                            intent = new Intent(MainActivity.this, AlarmReceiver.class);
                            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC, calGetup.getTimeInMillis(), pendingIntent);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            alarmManager.setExact(0, calGetup.getTimeInMillis(), pendingIntent);
                                alarmManager.setExact(0, 0, pendingIntent);
                            } else {
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(),
                                        calGetup.getTimeInMillis(), pendingIntent);
                            }

                            createNotification(getApplicationContext(), calGetup);
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "MANUAL",
                        (dialog, which) -> {
                            manual = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(this,
                                            calGetup.get(Calendar.HOUR_OF_DAY), calGetup.get(Calendar.MINUTE),
                                            getTime.equals(getTime24));
                            manual.setMinTime(calGetup.get(Calendar.HOUR_OF_DAY), calGetup.get(Calendar.MINUTE), 0);
                            manual.setMaxTime(fajrTime.get(Calendar.HOUR_OF_DAY), fajrTime.get(Calendar.MINUTE), 0);
                            manual.show(getFragmentManager(), "Timepickerdialog");
                            manual.dismissOnPause(true);
                        });
                alertDialog.show();
            }
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

    private void createNotification(Context context, Calendar time) {
        tvLastThird.setText("Alarm set for " + getTime.fn(time));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notifcation)
                .setContentTitle("Tahajjud alarm set")
                .setContentText(getTime.fn(time))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

//         To add a dismiss button
        Intent dismissIntent = new Intent(context, AlarmService.class);
        dismissIntent.setAction(AlarmService.ACTION_DISMISS);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context, 1, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action =
                new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm, "DISMISS", cancelPendingIntent);
        builder.addAction(action);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(2, notification);

        // persisting notification state
        SharedPreferences sp = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("alarmSet", true);
        editor.commit();
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
                    getTime = getTime24;
                    miTimeMode.setTitle(R.string.time_mode_pm);
                } else {
                    getTime = getTime12;
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
        fajrTime = Calendar.getInstance();
        fajrTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        fajrTime.set(Calendar.MINUTE, minute);
        fajrTime.add(Calendar.DATE, 1);
        fajrDisplay.setText(getTime.fn(fajrTime));

        FajrTime.getInstance().time = fajrTime;
    };

    TimePickerDialog.OnTimeSetListener maghribOnTimeSetListener = (view, hourOfDay, minute) -> {
        maghribTime = Calendar.getInstance();
        maghribTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        maghribTime.set(Calendar.MINUTE, minute);
        maghribDisplay.setText(getTime.fn(maghribTime));
    };

    GetTimeInterface getTime24 = (Calendar cal) -> {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(cal.getTime());
    };

    GetTimeInterface getTime12 = (Calendar cal) -> {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(cal.getTime());
    };

    void setAllTimeTextViews() {
        fajrDisplay.setText(getTime.fn(fajrTime));
        maghribDisplay.setText(getTime.fn(maghribTime));
        tvLastThird.setText("Alarm set for " + getTime.fn(calGetup));
        calcLastThird();
    }

    void calcLastThird() {
        long end = FajrTime.time.getTimeInMillis();
        long begin = maghribTime.getTimeInMillis();

        long third = (end - begin)/3;
        long getup = fajrTime.getTimeInMillis() - third;

        calGetup = Calendar.getInstance();

        calGetup.setTimeInMillis(getup);
    }

    void tvAlarmUnset() {
        tvLastThird.setText("Alarm not set");
    }

}
