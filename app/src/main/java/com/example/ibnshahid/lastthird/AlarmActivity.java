package com.example.ibnshahid.lastthird;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private Button cancel;
    private Button snooze;
    private TextView countdown;
    private AudioManager mAudioManager;
    private int originalVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

        cancel = (Button) findViewById(R.id.btn_stop);
        cancel.setOnClickListener(v -> {
            cancel();
        });

        snooze = (Button) findViewById(R.id.btn_snooze);
        snooze.setOnClickListener(v -> {
            cancel();
            //TODO snooze implementation
            Calendar in10 = Calendar.getInstance();
            in10.add(Calendar.MINUTE, 2);
            Utilities.setAlarm(this, in10);
        });

        showTimeLeft();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Utilities.player != null) cancel();
    }

    private void cancel() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);

        intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);

        if (Utilities.player.isPlaying()){
            Utilities.player.stop();
            Utilities.player.reset();
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, originalVolume, 0);
    }

    void showTimeLeft() {
        long timeleft = FajrTime.getInstance().time.getTimeInMillis() - System.currentTimeMillis();
        countdown = (TextView) findViewById(R.id.countdown);
        new CountDownTimer(timeleft, 1000) {
            public void onTick(long millisUntilFinished) {
                long hoursleft = millisUntilFinished / 1000 / 60 / 60;
                long minutes = (millisUntilFinished - hoursleft * 1000 * 60 * 60) / 1000 / 60;
                if (minutes / 10 == 0)
                    countdown.setText("time remaining: " + hoursleft + ":0" + minutes);
                else
                    countdown.setText("time remaining: " + hoursleft + ":" + minutes);
            }
            public void onFinish() {
                countdown.setText("Tahajjud time is over");
            }
        }.start();
        countdown.setText("" + FajrTime.getInstance().time.get(Calendar.HOUR_OF_DAY));
        countdown.setText("" + System.currentTimeMillis());
    }

}
