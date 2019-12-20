package com.android.dsly.common.base;

import android.app.Service;

import org.simple.eventbus.EventBus;

/**
 * @author 陈志鹏
 * @date 2019-12-07
 */
public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}