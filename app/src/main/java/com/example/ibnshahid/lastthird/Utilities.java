package com.example.ibnshahid.lastthird;

import android.app.Activity;
import android.app.AlarmManager;
import android.os.Build;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by ibnShahid on 01/08/2017.
 */

public class Utilities {
    private static void createNotification(Context context, Calendar time) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notifcation)
                .setContentTitle("Tahajjud alarm set")
                .setContentText(getTime.fn(time))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // To add a dismiss button
        Intent dismissIntent = new Intent(context, AlarmService.class);
        dismissIntent.setAction(AlarmService.ACTION_DISMISS);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context, 1, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action action =
                new NotificationCompat.Action(android.R.drawable.ic_lock_idle_alarm, "DISMISS", cancelPendingIntent);
        builder.addAction(action);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        // notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(2, notification);

        // persisting notification state
        SharedPreferences sp = context.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("alarmSet", true);
        editor.commit();
    }

    public static void setAlarm(Context context, Calendar calGetup) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calGetup.getTimeInMillis(), pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, calGetup.getTimeInMillis(), pendingIntent);
            createNotification(context, calGetup);
    }

    interface GetTimeInterface {
        String fn(Calendar cal);
    }

    static GetTimeInterface getTime = null;

    static GetTimeInterface getTime24 = (Calendar cal) -> {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(cal.getTime());
    };

    static GetTimeInterface getTime12 = (Calendar cal) -> {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(cal.getTime());
    };

    public static MediaPlayer player;
    public static void playAlarm(Context context) {
        dismissNotification(context);
        int resID = context.getResources().getIdentifier("alarm", "raw", context.getPackageName());
        player = MediaPlayer.create(context, resID);
        player.setLooping(true);
        player.start();
    }

    private static void dismissNotification(Context context) {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(2);
    }
}
