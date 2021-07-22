package com.android.dsly.common.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.android.dsly.common.R;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * @author 陈志鹏
 * @date 2018/12/21
 */
public class NotificationChannels {

    public final static String CHANNEL_HIGH = "high";
    public final static String CHANNEL_LOW = "low";
    private final static String GROUP_ID = "category";
    private final static String GROUP_NAME = "通知类别";

    public static void createAllNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (nm == null) {
                return;
            }
            NotificationChannelGroup group = new NotificationChannelGroup(GROUP_ID, GROUP_NAME);
            nm.createNotificationChannelGroup(group);

            NotificationChannel importantChannel = new NotificationChannel(CHANNEL_HIGH, "重要的通知", NotificationManager.IMPORTANCE_HIGH);
            // 开启指示灯，如果设备有的话
            importantChannel.enableLights(true);
            // 设置指示灯颜色
            importantChannel.setLightColor(ContextCompat.getColor(context, R.color._81D8CF));
            // 是否在久按桌面图标时显示此渠道的通知
            importantChannel.setShowBadge(true);
            // 设置是否应在锁定屏幕上显示此频道的通知
            importantChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            // 设置绕过免打扰模式
            importantChannel.setBypassDnd(true);
            //发出通知时的声音，默认用系统音效
//            importantChannel.setSound(null, null);
            //是否可以震动
            importantChannel.enableVibration(true);
            importantChannel.setGroup(GROUP_ID);
            nm.createNotificationChannel(importantChannel);

            NotificationChannel normalChannel = new NotificationChannel(CHANNEL_LOW, "正常的通知", NotificationManager.IMPORTANCE_LOW);
            normalChannel.setGroup(GROUP_ID);
            nm.createNotificationChannel(normalChannel);
        }
    }
}