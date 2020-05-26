package com.htxtdshopping.htxtd.frame.ui.first.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
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
import com.htxtdshopping.htxtd.frame.ui.first.activity.RefreshAndLoadMoreActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.RxjavaActivity;
import com.htxtdshopping.htxtd.frame.ui.first.activity.WebSocketActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public class FirstFragment extends BaseLazyFragment<FragmentFirstBinding, BaseViewModel> implements  View.OnClickListener {

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
                            Postcard postcard = ARouter.getInstance().build(RouterHub.ZXING_CAPTURE_ACTIVITY);
                            LogisticsCenter.completion(postcard);
                            Intent intent = new Intent(getActivity(), postcard.getDestination());
                            intent.putExtras(postcard.getExtras());
                            startActivityForResult(intent, CODE_SCAN_QR);
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
                String scanResult = data.getStringExtra(RouterHub.ZXING_RESULT_KEY_SCAN_RESULT);
                ToastUtils.showLong(scanResult);
                break;
            default:
                break;
        }
    }
}
