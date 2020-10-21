package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.htxtdshopping.htxtd.frame.R;

public class SlideCloseActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_slide_close;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean isSupportSwipeClose() {
        return true;
    }
}