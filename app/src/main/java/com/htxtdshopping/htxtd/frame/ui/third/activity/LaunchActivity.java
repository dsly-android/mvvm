package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityLaunchBinding;

public class LaunchActivity extends BaseActivity<ActivityLaunchBinding, BaseViewModel> {

    public static final String KEY_CONTENT = "key_content";

    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String content = getIntent().getStringExtra(KEY_CONTENT);
        mBinding.tvContent.setText(content);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
