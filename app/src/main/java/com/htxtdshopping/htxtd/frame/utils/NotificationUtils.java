package com.htxtdshopping.htxtd.frame.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.Utils;

import androidx.core.app.NotificationCompat;

/**
 * @author 陈志鹏
 * @date 2019-08-08
 */
public class NotificationUtils {
    private NotificationUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void createProgressNotification(int id, boolean ongoing, String channelId, int progress,
                                                  int smallIcon, String contentTitle, String contentText) {
        NotificationManager manager =
                (NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);

        // Cria a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setOngoing(ongoing)
                .setProgress(100, progress, false)
                .setAutoCancel(true);

        // Dispara a notification
        Notification n = builder.build();
        manager.notify(id, n);
    }

    public static void createSimpleNotification(int id, boolean ongoing, String channelId, Intent intent,
                                                int smallIcon, String contentTitle, String contentText) {
        NotificationManager manager =
                (NotificationManager) Utils.getApp().getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent para disparar o broadcast
        PendingIntent p = intent != null ? PendingIntent.getActivity(Utils.getApp(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT) : null;

        // Cria a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Utils.getApp(), channelId)
                .setContentIntent(p)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(smallIcon)
                .setOngoing(ongoing)
                .setAutoCancel(true);

        // Dispara a notification
        Notification n = builder.build();
        manager.notify(id, n);
    }
}
