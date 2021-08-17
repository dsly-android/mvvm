package com.android.dsly.common.constant;

/**
 * @author 陈志鹏
 * @date 2018/3/14
 */

public interface EventBusTag {
    //websocket接收到的消息
    String EVENT_RECEIVE_MESSAGE = "EVENT_RECEIVE_MESSAGE";
    //websocket发送的消息
    String EVENT_SEND_MESSAGE = "EVENT_SEND_MESSAGE";
    //网络状态改变发送的消息
    String EVENT_NETWORK_STATUS_CHANGED = "EVENT_NETWORK_STATUS_CHANGED";
    //收到阿里云推送通知的消息
    String EVENT_ALIPUSH_NOTICE = "EVENT_ALIPUSH_NOTICE";
    //token失效消息
    String EVENT_TOKEN_INVALIDATION = "EVENT_TOKEN_INVALIDATION";
}