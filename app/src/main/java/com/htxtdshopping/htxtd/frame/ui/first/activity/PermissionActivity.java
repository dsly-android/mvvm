package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BasePermissionConsumer;
import com.htxtdshopping.htxtd.frame.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * @author chenzhipeng
 */
public class PermissionActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_permission;
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

    public void click(View view) {
        new RxPermissions(this)
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new BasePermissionConsumer(){
                    @Override
                    public void success() {

                    }
                });
    }
}