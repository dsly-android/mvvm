package com.htxtdshopping.htxtd.frame.utils;

import com.tencent.mmkv.MMKV;

/**
 * 通过userId保存对应用户的数据
 *
 * @author 陈志鹏
 * @date 2021/1/5
 */
public class AppUserSPUtils {

    private static MMKV mmkv;

    public static String getUserId() {
        return "userId";
    }

    /**
     * 初始化数据
     */
    public static boolean isInitData() {
        return getMmkv().decodeBool("isInitData");
    }

    public static void putInitData(boolean isInitData) {
        getMmkv().encode("isInitData", isInitData);
    }

    private static MMKV getMmkv() {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(getUserId(), MMKV.MULTI_PROCESS_MODE);
        }
        return mmkv;
    }
}