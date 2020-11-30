package com.htxtdshopping.htxtd.frame.utils;

import com.blankj.utilcode.util.SPUtils;

/**
 * @author 陈志鹏
 * @date 2020/10/22
 */
public class AppSPUtils {

    public static final String USER_INFO = "user_info";

    public static void putUserId(Long userId) {
        SPUtils.getInstance(USER_INFO).put("userId", userId);
    }

    public static Long getUserId() {
        return SPUtils.getInstance(USER_INFO).getLong("userId");
    }

    public void clearUserInfo(){
        SPUtils.getInstance(USER_INFO).clear();
    }
}