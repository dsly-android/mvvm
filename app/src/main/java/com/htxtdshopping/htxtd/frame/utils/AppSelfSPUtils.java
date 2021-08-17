package com.htxtdshopping.htxtd.frame.utils;

import com.tencent.mmkv.MMKV;

/**
 * 退出登录后需要清空的数据
 *
 * @author 陈志鹏
 * @date 2020/10/22
 */
public class AppSelfSPUtils {

    public static final String USER_INFO = "user_info";
    private static MMKV mmkv;

    public static void putUserId(Long userId) {
        getMmkv().encode("userId", userId);
    }

    public static Long getUserId() {
        return getMmkv().decodeLong("userId");
    }

    public static void clear(){
        getMmkv().clearAll();
    }

    private static MMKV getMmkv() {
        if (mmkv == null) {
            mmkv = MMKV.mmkvWithID(USER_INFO, MMKV.MULTI_PROCESS_MODE);
        }
        return mmkv;
    }
}