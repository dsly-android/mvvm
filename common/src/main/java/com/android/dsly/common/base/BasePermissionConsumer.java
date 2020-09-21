package com.android.dsly.common.base;

import android.content.DialogInterface;

import com.android.dsly.common.dialog.ConfirmDialog;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.tbruyelle.rxpermissions2.Permission;

import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2020/9/16
 */
public abstract class BasePermissionConsumer implements Consumer<Permission> {

    @Override
    public void accept(Permission permission) {
        if (permission.granted) {
            success();
        } else if (permission.shouldShowRequestPermissionRationale) {
            ToastUtils.showLong("获取权限失败，无法使用相应功能");
        } else {
            showPermissionDeniedDialog();
        }
    }

    /**
     * 获取权限成功
     */
    public abstract void success();

    private void showPermissionDeniedDialog(){
        new ConfirmDialog.Builder(ActivityUtils.getTopActivity())
                .setContentStr("请在手机的权限管理里找到相关权限，并允许"+ AppUtils.getAppName()+"访问")
                .setPromptVisible(true)
                .setOnClickListener(new ConfirmDialog.OnClickListener() {
                    @Override
                    public void onPromptClick(DialogInterface dialog) {
                        super.onPromptClick(dialog);
                        PermissionUtils.launchAppDetailsSettings();
                    }
                }).create().show();
    }
}