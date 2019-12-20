package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.notification.Notifications;

import butterknife.OnClick;

public class NotificationActivity extends BaseFitsWindowActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification;
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

    @OnClick({R.id.btn_high, R.id.btn_low, R.id.btn_custom})
    public void onViewClicked(View view) {
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
