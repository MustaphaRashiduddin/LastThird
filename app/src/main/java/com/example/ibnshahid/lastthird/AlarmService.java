package com.example.ibnshahid.lastthird;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by ibnShahid on 06/05/2017.
 */

public class AlarmService extends Service {

    public static final String URI_BASE = AlarmService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";
    private Ringtone ringtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (ACTION_DISMISS.equals(action)) {
            dismissAlarm();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(2);
        } else {
            Intent alarm_intent = new Intent(this, AlarmActivity.class);
            startActivity(alarm_intent);
        }

        return START_NOT_STICKY;
    }

    private void dismissAlarm() {
        Intent intent = new Intent(this, AlarmService.class);
        stopService(intent);

        intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ringtone != null) ringtone.stop();
    }
}
