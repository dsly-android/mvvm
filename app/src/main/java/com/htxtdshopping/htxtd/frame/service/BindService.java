package com.htxtdshopping.htxtd.frame.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.android.dsly.common.base.BaseService;
import com.blankj.utilcode.util.LogUtils;

import androidx.annotation.Nullable;

/**
 * @author 陈志鹏
 * @date 2019-11-19
 */
public class BindService extends BaseService {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e("onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.e("onBind");
        return new MyBinder();
    }

    public class MyBinder extends Binder {

        public void haha() {
            LogUtils.e("haha");
        }

        public String getValue(int index) {
            LogUtils.e("haha" + index);
            return "haha" + index;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.e("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy");
    }
}
