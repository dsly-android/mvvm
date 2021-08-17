package com.android.dsly.push;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.android.dsly.common.constant.EventBusTag;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.Map;

/**
 * @author 陈志鹏
 * @date 2021/7/8
 */
public class AliPushMessageReceiver extends MessageReceiver {

    /**
     * 推送通知的回调方法
     */
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {

    }

    /**
     * 推送消息的回调方法
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {

    }

    /**
     * 从通知栏打开通知的扩展处理
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        JSONObject jo = JSON.parseObject(extraMap);
        String chatType = jo.getString("chatType");
        Long otherId = jo.getLong("otherId");

        AliPushEvent event = new AliPushEvent(chatType, otherId);
        LiveEventBus.get(EventBusTag.EVENT_ALIPUSH_NOTICE).post(event);
    }

    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {

    }

    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {

    }

    /**
     * 通知删除回调
     */
    @Override
    protected void onNotificationRemoved(Context context, String messageId) {

    }
}