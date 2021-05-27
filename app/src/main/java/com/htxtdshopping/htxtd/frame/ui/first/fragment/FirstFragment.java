package com.htxtdshopping.htxtd.frame.ui.first.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.FragmentFirstBinding;
import com.htxtdshopping.htxtd.frame.ui.first.activity.AutoSizeActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.BannerActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.FloatWindowActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.GenerateQrCodeActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.ObjectBoxActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.PermissionActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.PictureSelectorActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.RefreshAndLoadMoreActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.RxjavaActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.TakePhotoOrVideoActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.WebSocketActivity;
import com.htxtdshopping.htxtd.frame.ui.other.activity.ZxingActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public class FirstFragment extends BaseLazyFragment<FragmentFirstBinding, BaseViewModel> implements View.OnClickListener {

    public static final int CODE_SCAN_QR = 1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mBinding.vBar, getResources().getColor(R.color._81D8CF));
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
            case R.id.btn_refresh_and_load_more:
                ActivityUtils.startActivity(RefreshAndLoadMoreActivity.class);
                break;
            case R.id.btn_permission:
                ActivityUtils.startActivity(PermissionActivity.class);
                break;
            case R.id.btn_scanQrCode:
                requestPermission();
                break;
            case R.id.btn_generateQrCode:
                ActivityUtils.startActivity(GenerateQrCodeActivity.class);
                break;
            case R.id.btn_banner:
                ActivityUtils.startActivity(BannerActivity.class);
                break;
            case R.id.btn_rxjava:
                ActivityUtils.startActivity(RxjavaActivity.class);
                break;
            case R.id.btn_objectbox:
                ActivityUtils.startActivity(ObjectBoxActivity.class);
                break;
            case R.id.btn_autosize:
                ActivityUtils.startActivity(AutoSizeActivity.class);
                break;
            case R.id.btn_window:
                ActivityUtils.startActivity(FloatWindowActivity.class);
                break;
            case R.id.btn_websocket:
                ActivityUtils.startActivity(WebSocketActivity.class);
                break;
            case R.id.btn_picture_selector:
                ActivityUtils.startActivity(PictureSelectorActivity.class);
                break;
            case R.id.btn_camera:
                ActivityUtils.startActivity(TakePhotoOrVideoActivity.class);
                break;
            case R.id.btn_web:
                ARouter.getInstance()
                        .build(RouterHub.WEB_WEB_ACTIVITY)
                        .withString("url","http://www.baidu.com")
                        .navigation();
                break;
            default:
                break;
        }
    }

    private void requestPermission() {
        new RxPermissions(this)
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            ZxingActivity.start(getActivity());
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
            case FirstFragment.CODE_SCAN_QR:
                String scanResult = data.getStringExtra(ZxingActivity.RESULT_SCAN);
                ToastUtils.showLong(scanResult);
                break;
            default:
                break;
        }
    }
}
