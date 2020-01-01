package com.android.dsly.zxing.test;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.zxing.R;
import com.android.dsly.zxing.databinding.ZxingActivityMainBinding;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2019-12-13
 */
public class ZxingMainActivity extends BaseFitsWindowActivity<ZxingActivityMainBinding, BaseViewModel> {

    private static final int CODE_SCAN_QR = 1;

    @Override
    public int getLayoutId() {
        return R.layout.zxing_activity_main;
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
        requestPermission();
    }

    private void requestPermission() {
        new RxPermissions(this)
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            ARouter.getInstance().build(RouterHub.ZXING_CAPTURE_ACTIVITY)
                                    .navigation(ZxingMainActivity.this, CODE_SCAN_QR);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtils.showLong("shouldShowRequestPermissionRationale");
                        } else {
                            ToastUtils.showLong("OnNeverAskAgain");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_SCAN_QR:
                String scanResult = data.getStringExtra(RouterHub.ZXING_RESULT_KEY_SCAN_RESULT);
                ToastUtils.showLong(scanResult);
                break;
            default:
                break;
        }
    }
}
