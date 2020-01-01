package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityServiceBinding;
import com.htxtdshopping.htxtd.frame.service.BindService;
import com.htxtdshopping.htxtd.frame.service.LocalService;
import com.htxtdshopping.htxtd.frame.service.MyIntentService;

public class ServiceActivity extends BaseFitsWindowActivity<ActivityServiceBinding, BaseViewModel> implements View.OnClickListener {


    private Intent mIntent;
    private Intent mBindIntent;
    private Intent myIntentServiceIntent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mIntent = new Intent(this, LocalService.class);
        mBindIntent = new Intent(this, BindService.class);
        myIntentServiceIntent = new Intent(this, MyIntentService.class);
    }

    @Override
    public void initEvent() {
        mBinding.btnStartService.setOnClickListener(this);
        mBinding.btnStopService.setOnClickListener(this);
        mBinding.btnBindService.setOnClickListener(this);
        mBinding.btnUnbindService.setOnClickListener(this);
        mBinding.btnStartIntentService.setOnClickListener(this);
        mBinding.btnStopIntentService.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                    //8.0后系统不允许后台应用创建后台服务,可以调这个方法在前台启动服务
                    startForegroundService(mIntent);
                } else {
                    startService(mIntent);
                }
                break;
            case R.id.btn_stop_service:
                stopService(mIntent);
                break;
            case R.id.btn_bind_service:
                bindService(mBindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind_service:
                unbindService(serviceConnection);
                break;
            case R.id.btn_start_intent_service:
                //IntentService只能通过startService启动
                if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT) {
                    startForegroundService(myIntentServiceIntent);
                } else {
                    startService(myIntentServiceIntent);
                }
                break;
            case R.id.btn_stop_intent_service:
                stopService(myIntentServiceIntent);
                break;
            default:
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.MyBinder myBinder = (BindService.MyBinder) service;
            myBinder.haha();
            String value = myBinder.getValue(1);
            LogUtils.e(value);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
