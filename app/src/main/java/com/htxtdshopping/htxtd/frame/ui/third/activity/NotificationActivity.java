package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityNotificationBinding;
import com.htxtdshopping.htxtd.frame.notification.Notifications;

public class NotificationActivity extends BaseFitsWindowActivity<ActivityNotificationBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.btnHigh.setOnClickListener(this);
        mBinding.btnLow.setOnClickListener(this);
        mBinding.btnCustom.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_high:
                Notifications.getInstance().showImportantNotification(NotificationActivity.this);
                break;
            case R.id.btn_low:
                Notifications.getInstance().showNormalNotification(this);
                break;
            case R.id.btn_custom:
                Notifications.getInstance().showCustomViewNotification(this,true,true);
                break;
            default:
                break;
        }
    }
}