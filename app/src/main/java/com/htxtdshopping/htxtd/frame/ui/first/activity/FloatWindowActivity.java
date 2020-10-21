package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityFloatWindowBinding;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.permission.PermissionUtils;

import androidx.appcompat.app.AlertDialog;

public class FloatWindowActivity extends BaseActivity<ActivityFloatWindowBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_float_window;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_float:
                checkPermission();
                break;
            case R.id.btn_show_float:
                EasyFloat.showAppFloat(this);
                break;
            case R.id.btn_hide_float:
                EasyFloat.hideAppFloat(this);
                break;
            case R.id.btn_cancel_float:
                EasyFloat.dismissAppFloat(this);
                break;
            default:
                break;
        }
    }

    /**
     * 检测浮窗权限是否开启，若没有给与申请提示框（非必须，申请依旧是EasyFloat内部内保进行）
     */
    private void checkPermission() {
        if (PermissionUtils.checkPermission(this)) {
            showAppFloat();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                    .setPositiveButton("去开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showAppFloat();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    private void showAppFloat() {
        EasyFloat.with(this)
                .setLayout(R.layout.float_test)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .startForeground(true)
                .show();
    }
}
