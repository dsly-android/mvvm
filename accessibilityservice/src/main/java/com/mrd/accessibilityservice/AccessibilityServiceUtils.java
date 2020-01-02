package com.mrd.accessibilityservice;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

/**
 * @author 陈志鹏
 * @date 2020-01-01
 */
public class AccessibilityServiceUtils {

    // 此方法用来判断当前应用的辅助功能服务是否开启
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            LogUtils.e(e);
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }

    // 引导至辅助功能设置页面
    public static void toSetting(){
        Utils.getApp().startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}
