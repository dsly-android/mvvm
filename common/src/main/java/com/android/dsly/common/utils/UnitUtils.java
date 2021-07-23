package com.android.dsly.common.utils;

/**
 * @author 陈志鹏
 * @date 2021/7/23
 */
public class UnitUtils {

    public static String getDistanceStr(int distance) {
        if (distance < 1000) {
            return distance + "米";
        } else {
            return CalculationUtils.div(distance, 1000, 2) + "千米";
        }
    }
}
