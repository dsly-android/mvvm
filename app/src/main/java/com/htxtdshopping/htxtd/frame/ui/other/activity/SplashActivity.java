package com.htxtdshopping.htxtd.frame.ui.other.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;

import androidx.annotation.Nullable;

/**
 * @author chenzhipeng
 */
public class SplashActivity extends Activity {

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        BarUtils.setStatusBarVisibility(this, false);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                ActivityUtils.startActivity(MainActivity.class);
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }
}