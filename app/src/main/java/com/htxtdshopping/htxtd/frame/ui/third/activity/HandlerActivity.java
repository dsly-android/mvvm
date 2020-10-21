package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.dsly.common.base.BaseActivity;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.R;

import java.lang.ref.WeakReference;

public class HandlerActivity extends BaseActivity {

    private Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_handler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //在Java中，非静态内部类会隐性地持有外部类的引用，静态内部类则不会
        //如果持有外部类的引用，则内部类可以调用外部类的属性和方法
        //这样不会内存泄露，因为不是内部类，不会持有外部类的引用
//        mHandler = new Handler();
        //这样会内存泄露，因为它初始化了一个匿名内部类，会持有外部类的引用
        /*mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };*/
        //会泄露
//        mHandler = new LeakHandler(this);
        //会泄露
        /*mHandler = new LeakHandler(this){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };*/
        //不会泄露
        mHandler = new NoLeakHandler(this);
        //会泄露
        /*mHandler = new NoLeakHandler(this){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };*/
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        mHandler.sendEmptyMessageDelayed(0, 5 * 1000);
    }

    public class LeakHandler extends Handler {
        private WeakReference<HandlerActivity> mActivity;

        public LeakHandler(HandlerActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }

    public static class NoLeakHandler extends Handler {

        private WeakReference<HandlerActivity> mActivity;

        public NoLeakHandler(HandlerActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtils.e("handleMessage:" + (mActivity.get() == null));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(0);
    }
}