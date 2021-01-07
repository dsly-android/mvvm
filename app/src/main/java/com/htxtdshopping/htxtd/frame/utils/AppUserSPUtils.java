package com.htxtdshopping.htxtd.frame.utils;

import com.blankj.utilcode.util.SPUtils;

/**
 * 通过userId保存对应用户的数据
 *
 * @author 陈志鹏
 * @date 2021/1/5
 */
public class AppUserSPUtils {

    public static String getUserId() {
        return "userId";
    }

    /**
     * 初始化数据
     */
    public boolean isInitData() {
        return SPUtils.getInstance(getUserId()).getBoolean("isInitData");
    }

    public void putInitData(boolean isInitData) {
        SPUtils.getInstance(getUserId()).put("isInitData", isInitData);
    }
}