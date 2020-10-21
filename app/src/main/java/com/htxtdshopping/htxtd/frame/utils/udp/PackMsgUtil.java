package com.htxtdshopping.htxtd.frame.utils.udp;

/**
 * 报文构建类
 */
public class PackMsgUtil {
    public static String submitAddress(String roomId, String userId) {
//        SUBMIT_ADDRESS|roomId|userId
        return "SUBMIT_ADDRESS|" + roomId + "|" + userId;
    }

    public static String ping(String roomId, String userId) {
        return "PING|" + roomId + "|" + userId;
    }

    public static String submitStream(String roomId, String userId, String stream) {
//        SUBMIT_STREAM|roomId|userId|START|数据流字符串|END
        return "SUBMIT_STREAM|" + roomId + "|" + userId + "|START|" + stream + "|END";
    }
}
