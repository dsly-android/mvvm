package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityTakePhotoOrVideoBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.functions.Consumer;

public class TakePhotoOrVideoActivity extends BaseActivity<ActivityTakePhotoOrVideoBinding, BaseViewModel> {

    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
    public static final String KEY_VIDEO_PATH = "KEY_VIDEO_PATH";
    public static final String KEY_FIRST_FRAME_PATH = "KEY_FIRST_FRAME_PATH";

    public static final String TYPE_IMAGE = "IMAGE";
    public static final String TYPE_VIDEO = "VIDEO";

    @Override
    public int getLayoutId() {
        return R.layout.activity_take_photo_or_video;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    public void initEvent() {
        //设置视频保存路径
        mBinding.jcvCamera.setSaveVideoPath(getExternalCacheDir() + File.separator + "JCamera");
        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        mBinding.jcvCamera.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        //设置视频质量
        mBinding.jcvCamera.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        //JCameraView监听
        mBinding.jcvCamera.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                finish();
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                finish();
            }
        });
        mBinding.jcvCamera.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                File file = new File(getExternalCacheDir() + File.separator + "JCamera"
                        + File.separator + System.currentTimeMillis() + ".png");
                ImageUtils.save(bitmap, file, Bitmap.CompressFormat.PNG);

                Intent intent = new Intent();
                intent.putExtra(KEY_TYPE, TYPE_IMAGE);
                intent.putExtra(KEY_IMAGE_PATH, file.getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                File firstFrameFile = new File(getExternalCacheDir() + File.separator + "JCamera"
                        + File.separator + System.currentTimeMillis() + ".png");
                ImageUtils.save(firstFrame, firstFrameFile, Bitmap.CompressFormat.PNG);

                Intent intent = new Intent();
                intent.putExtra(KEY_TYPE, TYPE_VIDEO);
                intent.putExtra(KEY_FIRST_FRAME_PATH, firstFrameFile.getAbsolutePath());
                intent.putExtra(KEY_VIDEO_PATH, url);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //左边按钮点击事件
        mBinding.jcvCamera.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mBinding.jcvCamera.onResume();
                        } else {
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)) {
            mBinding.jcvCamera.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.jcvCamera.onPause();
    }

    @Override
    protected void initStatusBar() {
        BarUtils.setStatusBarColor(this, Color.TRANSPARENT);
    }

    @Override
    protected boolean isFitWindow() {
        return false;
    }
}