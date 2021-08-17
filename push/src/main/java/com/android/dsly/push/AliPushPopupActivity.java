package com.android.dsly.push;

import android.os.Bundle;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.android.dsly.common.constant.EventBusTag;
import com.blankj.utilcode.util.AppUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.Map;

public class AliPushPopupActivity extends AndroidPopupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //也可以使用这种方式
        /*(new PopupNotifyClick(new PopupNotifyClickListener() {
            public void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
                LogUtils.e( "main Receive notification, title: " + title + ", content: " + summary + ", extraMap: " + extMap);
            }
        })).onCreate(this, this.getIntent());*/
    }

    /**
     * 实现通知打开回调方法，获取通知相关信息
     * @param title     标题
     * @param summary   内容
     * @param extMap    额外参数
     */
    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extMap) {
        AppUtils.launchApp(getPackageName());

        String chatType = extMap.get("chatType");
        Long otherId = Long.valueOf(extMap.get("otherId"));

        AliPushEvent event = new AliPushEvent(chatType, otherId);
        PushContext.getInstance().setAliPushEvent(event);
        LiveEventBus.get(EventBusTag.EVENT_ALIPUSH_NOTICE).post(event);

        finish();
    }
}