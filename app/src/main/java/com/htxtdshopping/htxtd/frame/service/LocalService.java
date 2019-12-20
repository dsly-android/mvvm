package com.htxtdshopping.htxtd.frame.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.android.dsly.common.base.BaseService;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.notification.Notifications;

import androidx.annotation.Nullable;

/**
 * @author 陈志鹏
 * @date 2018/12/25
 */
public class LocalService extends BaseService {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("onCreate");
        //8.0后通过startForegroundService启动一个service，在系统创建服务后，
        // 应用有五秒的时间来调用该服务的 startForeground() 方法以显示新服务的用户可见通知。
        //如果应用在此时间限制内未调用 startForeground()，则系统将停止服务并声明此应用为 ANR。
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            Notifications.getInstance().showForegroundNotification(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.e("onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("onStartCommand");
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
        if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
            stopForeground(true);
        }
    }
}