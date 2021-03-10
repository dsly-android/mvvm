package com.htxtdshopping.htxtd.frame.ui.other.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RequestCode;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityZxingBinding;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;

public class ZxingActivity extends BaseActivity<ActivityZxingBinding, BaseViewModel> implements QRCodeView.Delegate, View.OnClickListener {

    public static final String RESULT_SCAN = "RESULT_SCAN";
    //选择文件中的图片
    public static final int CODE_SCAN_GALLERY = 1;

    private boolean mIsFlashLight = false;

    public static void start(FragmentActivity activity) {
        Intent intent = new Intent(activity, ZxingActivity.class);
        activity.startActivityForResult(intent, RequestCode.REQUEST_SCAN_CODE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_zxing;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.tbTitle.setRightTextString("相册");

        mBinding.zvView.setDelegate(this);
    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
        mBinding.tbTitle.setOnRightTextClickListener(new ClickUtils.OnDebouncingClickListener(false) {
            @Override
            public void onDebouncingClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, CODE_SCAN_GALLERY);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtils.i("扫描结果：" + result);
        VibrateUtils.vibrate(200);
        Intent intent = new Intent();
        intent.putExtra(RESULT_SCAN, result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mBinding.zvView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mBinding.zvView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mBinding.zvView.getScanBoxView().setTipText(tipText);
            }
        }
        isDarkEnv(isDark);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtils.e("打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_SCAN_GALLERY:
                File file = UriUtils.uri2File(data.getData());
                // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
                mBinding.zvView.decodeQRCode(file.getAbsolutePath());
                break;
            default:
                break;
        }
    }

    public void isDarkEnv(boolean isDarkEnv) {
        if (isDarkEnv || mIsFlashLight) {
            mBinding.ivLightSwitch.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivLightSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_light_switch:
                if (mIsFlashLight) {
                    mIsFlashLight = false;
                    mBinding.zvView.closeFlashlight();
                    mBinding.ivLightSwitch.setImageResource(R.drawable.zxing_torch_on);
                } else {
                    mIsFlashLight = true;
                    mBinding.zvView.openFlashlight();
                    mBinding.ivLightSwitch.setImageResource(R.drawable.zxing_torch_off);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBinding.zvView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mBinding.zvView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mBinding.zvView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mBinding.zvView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }
}