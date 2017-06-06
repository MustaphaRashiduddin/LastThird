package com.example.ibnshahid.lastthird;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class AlarmActivity extends AppCompatActivity {

    private Ringtone ringtone;
    private Button cancel;
    private Button snooze;
    private TextView countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        dismissNotification(this);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (uri == null) uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();

        cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(v -> {
            cancel();
        });

        snooze = (Button) findViewById(R.id.btn_snooze);
        snooze.setOnClickListener(v -> {
            Toast.makeText(this, "implement", Toast.LENGTH_SHORT).show();
//            cancel();
            //TODO snooze implementation
        });

        long timeleft = FajrTime.getInstance().time.getTimeInMillis() - System.currentTimeMillis();
        countdown = (TextView) findViewById(R.id.countdown);
        new CountDownTimer(timeleft, 1000) {

            public void onTick(long millisUntilFinished) {
                long hoursleft = millisUntilFinished / 1000 / 60 / 60;
                long minutes = (millisUntilFinished - hoursleft * 1000 * 60 * 60) / 1000 / 60;
                countdown.setText("time remaining: " + hoursleft + ":" + minutes);
            }

            public void onFinish() {
                countdown.setText("IT'S FAJR TIME!");
            }
        }.start();
        countdown.setText("" + FajrTime.getInstance().time.get(Calendar.HOUR_OF_DAY));
        countdown.setText("" + System.currentTimeMillis());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ringtone != null) cancel();
    }

    private void cancel() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);

        intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        if (ringtone != null) ringtone.stop();

    }

    private void dismissNotification(Context context) {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(2);
    }

}
