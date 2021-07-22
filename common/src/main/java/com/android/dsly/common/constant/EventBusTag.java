package com.android.dsly.common.constant;

/**
 * @author 陈志鹏
 * @date 2018/3/14
 */

public interface EventBusTag {
    //websocket接收到的消息
    String EVENT_RECEIVE_MESSAGE = "event_receive_message";
    //websocket发送的消息
    String EVENT_SEND_MESSAGE = "event_send_message";

    //网络状态改变发送的消息
    String EVENT_NETWORK_STATUS_CHANGED = "event_network_status_changed";

    //收到阿里云推送通知的消息
    String EVENT_ALIPUSH_NOTICE = "EVENT_ALIPUSH_NOTICE";
}
