package com.htxtdshopping.htxtd.frame.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.blankj.utilcode.util.VibrateUtils;

/**
 * 声音与震动
 *
 * @author 陈志鹏
 * @date 2021/1/12
 */
public class PromptUtils {

    /**
     * 声音与震动
     */
    public static void play(Context context){
        beep(context);
        vibrate();
    }

    /**
     * 系统提示音
     */
    public static void beep(Context context){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        if (!r.isPlaying()){
            r.play();
        }
    }

    /**
     * 简单震动
     */
    public static void vibrate() {
        VibrateUtils.vibrate(200);
    }
}
