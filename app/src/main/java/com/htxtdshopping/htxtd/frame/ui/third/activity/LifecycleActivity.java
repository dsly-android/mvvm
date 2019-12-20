package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.lifecycle.TestObserver;

public class LifecycleActivity extends BaseFitsWindowActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_lifecycle;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //添加后可以不调用移除方法，源码中已在onDestroy时调用了移除方法
        getLifecycle().addObserver(new TestObserver());
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        LogUtils.e("onResume:start");
        super.onResume();
        LogUtils.e("onResume:stop");
    }
}
