package com.maihealthtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static RemoteViews contentView;
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static final int NotificationID = 1005;
    private static NotificationCompat.Builder mBuilder;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context, "notify_001");

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        mBuilder.setSound(alarmSound);
        mBuilder.build().flags = Notification.FLAG_AUTO_CANCEL | Notification.PRIORITY_HIGH;
        mBuilder.setAutoCancel(true);
        mBuilder.setShowWhen(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "mai health tracker", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
            mBuilder.setAutoCancel(true);

        }



        notificationManager.notify(NotificationID, notification);

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        11,
                        i,
                        PendingIntent.FLAG_ONE_SHOT
                );

        mBuilder.setContentIntent(pendingIntent);
        notification = mBuilder.build();

    }

}
