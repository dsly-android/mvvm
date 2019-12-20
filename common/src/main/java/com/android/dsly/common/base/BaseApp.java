package com.android.dsly.common.base;

import android.content.Context;

import com.android.dsly.common.di.component.DaggerBaseComponent;
import com.android.dsly.common.core.AppDelegate;
import com.android.dsly.common.core.AppLifecycles;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

/**
 * @author 陈志鹏
 * @date 2019-12-09
 */
public class BaseApp extends DaggerApplication {

    private AppLifecycles mAppDelegate;

    /**
     * 这里会在 {@link BaseApp#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null)
            this.mAppDelegate = new AppDelegate(this);
        this.mAppDelegate.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null)
            this.mAppDelegate.onCreate(this);
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerBaseComponent.builder().build();
    }
}
