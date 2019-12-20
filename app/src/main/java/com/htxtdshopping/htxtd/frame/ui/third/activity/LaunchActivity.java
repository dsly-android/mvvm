package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.htxtdshopping.htxtd.frame.R;

import butterknife.BindView;

public class LaunchActivity extends BaseActivity {

    public static final String KEY_CONTENT = "key_content";

    @BindView(R.id.tv_content)
    TextView mTvContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String content = getIntent().getStringExtra(KEY_CONTENT);
        mTvContent.setText(content);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
