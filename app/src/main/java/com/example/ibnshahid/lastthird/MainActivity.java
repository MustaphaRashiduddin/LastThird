package com.example.ibnshahid.lastthird;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView fajrDisplay = null;
    private TextView maghribDisplay = null;
    private TextView tvLastThird = null;
    private Calendar fajrTime = Calendar.getInstance();
    private Calendar maghribTime = Calendar.getInstance();
    Calendar calGetup = Calendar.getInstance();
    MenuItem miTimeMode = null;

    private PendingIntent pendingIntent = null;
    private Intent intent = null;
    AlarmManager alarmManager = null;

    interface GetTimeInterface {
        String fn(Calendar cal);
    }

    GetTimeInterface getTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getTime = getTime12;


        fajrDisplay = (TextView) findViewById(R.id.tv_show_fajr_time);
        Button fajrDisplayTPDButton = (Button) findViewById(R.id.btn_pic_fajr_time);
        fajrDisplayTPDButton.setOnClickListener(v -> new TimePickerDialog(MainActivity.this, fajrOnTimeSetListener, 5,
                0, getTime.equals(getTime24)).show());

        maghribDisplay = (TextView) findViewById(R.id.tv_show_maghrib_time);
        Button maghribDisplayTPDButton = (Button) findViewById(R.id.btn_pic_maghrib_time);
        maghribDisplayTPDButton.setOnClickListener(v -> new TimePickerDialog(MainActivity.this, maghribOnTimeSetListener, 18,
                0, getTime.equals(getTime24)).show());

        Button btnLastThird = (Button) findViewById(R.id.btn_last_third);
        btnLastThird.setOnClickListener(v -> calcLastThird());
        tvLastThird = (TextView) findViewById(R.id.tv_last_third);

        Button btnAlarm = (Button) findViewById(R.id.btn_set_alarm);
        btnAlarm.setOnClickListener(v -> {

            intent = new Intent(MainActivity.this, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, calGetup.getTimeInMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calGetup.getTimeInMillis(),
                    fajrTime.getTimeInMillis() - calGetup.getTimeInMillis(), pendingIntent);

            createNotification(getApplicationContext());
        });

        Button btnCancelAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
//        btnCancelAlarm.setOnClickListener(v -> {
//            Intent i = new Intent(getApplicationContext(), AlarmService.class);
//            stopService(i);
//
//            intent = new Intent(getApplicationContext(), AlarmReceiver.class);
//
//            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//            alarmManager.cancel(pendingIntent);
//        });
        btnCancelAlarm.setVisibility(View.INVISIBLE);

    }

    private void createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notifcation)
                .setContentTitle("Tahajjud alarm set")
                .setContentText(getTime.fn(calGetup))
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
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(2, notification);
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
        calcLastThird();
    }

    void calcLastThird() {
        long end = fajrTime.getTimeInMillis();
        long begin = maghribTime.getTimeInMillis();

        long third = (end - begin)/3;
        long getup = fajrTime.getTimeInMillis() - third;

        calGetup = Calendar.getInstance();

        calGetup.setTimeInMillis(getup);

        tvLastThird.setText(getTime.fn(calGetup));

    }

}
