package com.htxtdshopping.htxtd.frame.ui.other.activity;

import android.app.Activity;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;

import androidx.annotation.Nullable;

/**
 * @author chenzhipeng
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        BarUtils.setStatusBarVisibility(this, false);

        ActivityUtils.startActivity(MainActivity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}