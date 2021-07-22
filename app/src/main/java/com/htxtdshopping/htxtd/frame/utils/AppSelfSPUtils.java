package com.htxtdshopping.htxtd.frame.utils;

import com.blankj.utilcode.util.SPUtils;

/**
 * 退出登录后需要清空的数据
 *
 * @author 陈志鹏
 * @date 2020/10/22
 */
public class AppSelfSPUtils {

    public static final String USER_INFO = "user_info";

    public static void putUserId(Long userId) {
        SPUtils.getInstance(USER_INFO).put("userId", userId);
    }

    public static Long getUserId() {
        return SPUtils.getInstance(USER_INFO).getLong("userId");
    }

    public static void clear(){
        SPUtils.getInstance(USER_INFO).clear();
    }
}