package com.htxtdshopping.htxtd.frame.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.android.dsly.common.notification.NotificationChannels;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.third.activity.LaunchActivity;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author 陈志鹏
 * @date 2018/12/23
 */
public class Notifications {
    /**
     * 通知id
     */
    private static final int NOTIFICATION_IMPORTANT = 1;
    private static final int NOTIFICATION_NORMAL = 2;
    private static final int NOTIFICATION_CUSTOM = 3;
    private static final int NOTIFICATION_FOREGROUND_SERVICE = 4;

    public static Notifications getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static final class LazyHolder {
        private static final Notifications INSTANCE = new Notifications();
    }

    public void showImportantNotification(Context context) {
        Intent contentIntent = new Intent(context, LaunchActivity.class);
        contentIntent.putExtra(LaunchActivity.KEY_CONTENT, "点击通知");
        Intent deleteIntent = new Intent(context, LaunchActivity.class);
        deleteIntent.putExtra(LaunchActivity.KEY_CONTENT, "删除通知");
        //两个PendingIntent的requestCode如果一样，后面一个就会替换前面一个
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent deletePendingIntent = PendingIntent.getActivity(context, 1, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannels.CHANNEL_HIGH)
                //设置通知标题
                .setContentTitle("Important")
                //设置通知内容
                .setContentText("重要的通知")
                //设置点击通知后自动删除通知
                .setAutoCancel(true)
                //设置是否显示通知时间
                .setShowWhen(true)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //设置通知左侧的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知右侧的大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                //设置通知铃声
                .setSound(null)
                //设置通知不可删除
//                .setOngoing(true)
                //设置未读角标(实际测试不加这个系统也会自己统计数字)
//                .setNumber(3)
                //设置通知超时时间
                .setTimeoutAfter(5000)
                //设置震动
                .setVibrate(new long[]{100, 100, 100, 100})
                //设置点击通知时的响应事件
                .setContentIntent(contentPendingIntent)
                //设置删除通知时的响应事件
                .setDeleteIntent(deletePendingIntent);
        //发送通知
        manager.notify(NOTIFICATION_IMPORTANT, builder.build());
    }

    public void showNormalNotification(Context context) {
        Intent contentIntent = new Intent(context, LaunchActivity.class);
        contentIntent.putExtra(LaunchActivity.KEY_CONTENT, "点击通知");
        Intent deleteIntent = new Intent(context, LaunchActivity.class);
        deleteIntent.putExtra(LaunchActivity.KEY_CONTENT, "删除通知");
        //两个PendingIntent的requestCode如果一样，后面一个就会替换前面一个
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent deletePendingIntent = PendingIntent.getActivity(context, 1, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannels.CHANNEL_LOW)
                //设置通知标题
                .setContentTitle("normal")
                //设置通知内容
                .setContentText("正常的通知")
                //设置点击通知后自动删除通知
                .setAutoCancel(true)
                //设置是否显示通知时间
                .setShowWhen(true)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //设置通知左侧的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知右侧的大图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                //设置通知铃声
                .setSound(null)
                //设置震动
                .setVibrate(new long[]{100, 100, 100, 100})
                //设置点击通知时的响应事件
                .setContentIntent(contentPendingIntent)
                //设置删除通知时的响应事件
                .setDeleteIntent(deletePendingIntent);
        //发送通知
        manager.notify(NOTIFICATION_NORMAL, builder.build());
    }

    /**
     * 发送自定义通知
     */
    public void showCustomViewNotification(Context context, boolean isPlaying, boolean isLoved) {
        Intent contentIntent = new Intent(context, LaunchActivity.class);
        contentIntent.putExtra(LaunchActivity.KEY_CONTENT, "自定义布局");
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //创建各个按钮的点击响应广播
        Intent intentLove = new Intent(context, LaunchActivity.class);
        intentLove.putExtra(LaunchActivity.KEY_CONTENT, "点击了喜欢");
        PendingIntent piLove = PendingIntent.getActivity(context, 1, intentLove, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPre = new Intent(context, LaunchActivity.class);
        intentPre.putExtra(LaunchActivity.KEY_CONTENT, "点击了上一首");
        PendingIntent piPre = PendingIntent.getService(context, 2, intentPre, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlayOrPause = new Intent(context, LaunchActivity.class);
        intentPlayOrPause.putExtra(LaunchActivity.KEY_CONTENT, "点击了播放或暂停");
        PendingIntent piPlayOrPause = PendingIntent.getService(context, 3, intentPlayOrPause, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentNext = new Intent(context, LaunchActivity.class);
        intentNext.putExtra(LaunchActivity.KEY_CONTENT, "点击了下一首");
        PendingIntent piNext = PendingIntent.getService(context, 4, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentLyrics = new Intent(context, LaunchActivity.class);
        intentLyrics.putExtra(LaunchActivity.KEY_CONTENT, "点击了歌词");
        PendingIntent piLyrics = PendingIntent.getService(context, 5, intentLyrics, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentCancel = new Intent(context, LaunchActivity.class);
        intentLyrics.putExtra(LaunchActivity.KEY_CONTENT, "点击了删除通知");
        PendingIntent piCancel = PendingIntent.getService(context, 6, intentCancel, PendingIntent.FLAG_UPDATE_CURRENT);

        //创建自定义小视图
        RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.notification_custom_view);
        customView.setImageViewBitmap(R.id.iv_content, BitmapFactory.decodeResource(context.getResources(), R.drawable.custom_view_picture_current));
        customView.setTextViewText(R.id.tv_title, "最美的期待");
        customView.setTextViewText(R.id.tv_summery, "周笔畅");
        customView.setImageViewBitmap(R.id.iv_play_or_pause, BitmapFactory.decodeResource(context.getResources(),
                isPlaying ? R.drawable.ic_pause : R.drawable.ic_play));
        customView.setOnClickPendingIntent(R.id.iv_play_or_pause, piPlayOrPause);
        customView.setOnClickPendingIntent(R.id.iv_next, piNext);
        customView.setOnClickPendingIntent(R.id.iv_lyrics, piLyrics);
        customView.setOnClickPendingIntent(R.id.iv_cancel, piCancel);
        //创建自定义大视图
        RemoteViews customBigView = new RemoteViews(context.getPackageName(), R.layout.notification_big_custom_view);
        customBigView.setImageViewBitmap(R.id.iv_content_big, BitmapFactory.decodeResource(context.getResources(), R.drawable.custom_view_picture_current));
        customBigView.setTextViewText(R.id.tv_title_big, "最美的期待");
        customBigView.setTextViewText(R.id.tv_summery_big, "周笔畅");
        customBigView.setImageViewBitmap(R.id.iv_love_big, BitmapFactory.decodeResource(context.getResources(),
                isLoved ? R.drawable.ic_loved : R.drawable.ic_love));
        customBigView.setImageViewBitmap(R.id.iv_play_or_pause_big, BitmapFactory.decodeResource(context.getResources(),
                isPlaying ? R.drawable.ic_pause : R.drawable.ic_play));
        customBigView.setOnClickPendingIntent(R.id.iv_love_big, piLove);
        customBigView.setOnClickPendingIntent(R.id.iv_pre_big, piPre);
        customBigView.setOnClickPendingIntent(R.id.iv_play_or_pause_big, piPlayOrPause);
        customBigView.setOnClickPendingIntent(R.id.iv_next_big, piNext);
        customBigView.setOnClickPendingIntent(R.id.iv_lyrics_big, piLyrics);
        customBigView.setOnClickPendingIntent(R.id.iv_cancel_big, piCancel);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //创建通知
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context, NotificationChannels.CHANNEL_HIGH)
                //设置通知左侧的小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知标题
                .setContentTitle("Custom notification")
                //设置通知内容
                .setContentText("Demo for custom notification !")
                //设置通知不可删除
                .setOngoing(true)
                //设置显示通知时间
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())
                //设置点击通知时的响应事件
                .setContentIntent(contentPendingIntent)
                //设置自定义小视图
                .setCustomContentView(customView)
                //设置自定义大视图
                .setCustomBigContentView(customBigView);
        //发送通知
        manager.notify(NOTIFICATION_CUSTOM, nb.build());
    }

    /**
     * 前台服务通知
     */
    public void showForegroundNotification(Service service) {
        Intent contentIntent = new Intent(service, LaunchActivity.class);
        contentIntent.putExtra(LaunchActivity.KEY_CONTENT, "前台服务");
        PendingIntent contentPendingIntent = PendingIntent.getActivity(service, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(service, NotificationChannels.CHANNEL_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setTicker("Foreground service")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Foreground service")
                .setContentText("保持应用后台运行，能及时接收信息")
                .setContentIntent(contentPendingIntent)
                .build();
        service.startForeground(NOTIFICATION_FOREGROUND_SERVICE, notification);
    }
}