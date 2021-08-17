package com.android.dsly.push;

import android.content.Context;

/**
 * @author 陈志鹏
 * @date 2021/7/10
 */
public class PushContext {
    private static PushContext mPushContext;
    private Context mContext;

    public static void init(Context context) {
        if (mPushContext == null) {
            synchronized (PushContext.class) {
                if (mPushContext == null) {
                    mPushContext = new PushContext(context);
                }
            }
        }
    }

    public static PushContext getInstance() {
        return mPushContext;
    }

    public PushContext(Context context) {
        mContext = context;
    }

    private AliPushEvent mAliPushEvent;

    public void setAliPushEvent(AliPushEvent aliPushEvent) {
        this.mAliPushEvent = aliPushEvent;
    }

    public AliPushEvent getAliPushEvent() {
        return mAliPushEvent;
    }
}
