package com.example.xgramajo.parkme_ids_2018;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String timeLeftChannelID = "timeLeftChannelID";
    public static final String timeLeftChannelName = "Alerta de tiempo restante";

    public static final String chronometerChannelID = "chronometerChannelID";
    public static final String chronometerChannelName = "Cronómetro";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels() {
        // Create channel 1 for time left notifications
        NotificationChannel channel1 = new NotificationChannel(timeLeftChannelID, timeLeftChannelName, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel1);

        // Create channel 2 for chronometer notifications
        NotificationChannel channel2 = new NotificationChannel(chronometerChannelID, chronometerChannelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(false);
        channel2.enableVibration(false);
        channel2.setSound(null, null);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel2);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String title, String text) {
        return new NotificationCompat.Builder(getApplicationContext(), timeLeftChannelID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_time);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String text) {
        return new NotificationCompat.Builder(getApplicationContext(), chronometerChannelID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_time);
    }
}
