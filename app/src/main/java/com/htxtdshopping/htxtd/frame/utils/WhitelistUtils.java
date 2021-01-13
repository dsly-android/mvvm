package com.htxtdshopping.htxtd.frame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import com.blankj.utilcode.util.AppUtils;

import androidx.annotation.RequiresApi;

/**
 * @author 陈志鹏
 * @date 2021/1/13
 */
public class WhitelistUtils {

    /**
     * 加入白名单
     */
    public static void joinToWhitelist(Activity activity, Integer requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isIgnoringBatteryOptimizations(activity)) {
                requestIgnoreBatteryOptimizations(activity, requestCode);
            }
        }
    }

    /**
     * 判断我们的应用是否在白名单中
     *
     * @param activity
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isIgnoringBatteryOptimizations(Activity activity) {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(AppUtils.getAppPackageName());
        }
        return isIgnoring;
    }

    /**
     * 申请加入白名单
     *
     * @param activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestIgnoreBatteryOptimizations(Activity activity, Integer requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + AppUtils.getAppPackageName()));
            if (requestCode == null) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}