package com.android.dsly.biometrics.face;

import android.Manifest;
import android.content.res.Configuration;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.dsly.biometrics.R;
import com.android.dsly.biometrics.databinding.ActivityFaceDetectionBinding;
import com.baidu.aip.ImageFrame;
import com.baidu.aip.face.CameraImageSource;
import com.baidu.aip.face.DetectRegionProcessor;
import com.baidu.aip.face.FaceDetectManager;
import com.baidu.aip.face.PreviewView;
import com.baidu.aip.face.camera.ICameraControl;
import com.baidu.aip.face.camera.PermissionCallback;
import com.baidu.idl.facesdk.FaceInfo;
import com.blankj.utilcode.util.BrightnessUtils;
import com.blankj.utilcode.util.ScreenUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

public class FaceDetectionActivity extends AppCompatActivity {

    private ActivityFaceDetectionBinding mBinding;
    private Handler mHandler = new Handler();

    private FaceDetectManager faceDetectManager;
    private DetectRegionProcessor cropProcessor = new DetectRegionProcessor();
    private boolean mIsPortrait = true;
    private boolean mDetectStoped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_face_detection);

        mBinding.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initEvent();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!FaceDetectionActivity.this.isFinishing()) {
                    start();
                }
            }
        }, 500);
    }

    private void initEvent() {
        faceDetectManager = new FaceDetectManager(this);

        mBinding.textureView.setOpaque(false);
        // 不需要屏幕自动变黑。
        mBinding.textureView.setKeepScreenOn(true);

        initCamera();
        initBrightness();
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        // 初始化相机图片资源
        CameraImageSource cameraImageSource = new CameraImageSource(this);
        // 设置预览界面
        cameraImageSource.setPreviewView(mBinding.previewView);

        // 设置人脸检测图片资源
        faceDetectManager.setImageSource(cameraImageSource);
        // 设置人脸检测回调,其中 retCode为人脸检测回调值（0通常为检测到人脸),infos为人脸信息，frame为相机回调图片资源
        faceDetectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(final int retCode, FaceInfo[] infos, ImageFrame frame) {
                if (infos != null && infos[0] != null) {
                    Log.e("aaa", infos[0].mWidth + "  " + infos[0].face_id);
                } else {
                    Log.e("aaa", "没有检测到人脸");
                }
            }
        });

        cameraImageSource.getCameraControl().setPermissionCallback(new PermissionCallback() {
            @Override
            public boolean onRequestPermission() {
                ActivityCompat.requestPermissions(FaceDetectionActivity.this,
                        new String[]{Manifest.permission.CAMERA}, 100);
                return true;
            }
        });


        ICameraControl control = cameraImageSource.getCameraControl();
        control.setPreviewView(mBinding.previewView);
        // 设置检测裁剪处理器
        faceDetectManager.addPreProcessor(cropProcessor);

        // 获取相机屏幕方向
        int orientation = getResources().getConfiguration().orientation;
        mIsPortrait = (orientation == Configuration.ORIENTATION_PORTRAIT);

        // 根据屏幕方向决定预览拉伸类型
        if (mIsPortrait) {
            mBinding.previewView.setScaleType(PreviewView.ScaleType.FIT_WIDTH);
        } else {
            mBinding.previewView.setScaleType(PreviewView.ScaleType.FIT_HEIGHT);
        }

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        cameraImageSource.getCameraControl().setDisplayOrientation(rotation);
        // 设置相机摄像头类型，包括前置、后置及usb等类型
        setCameraType(cameraImageSource);
    }

    /**
     * 初始化屏幕亮度，不到200自动调整到200
     */
    private void initBrightness() {
        int brightness = BrightnessUtils.getWindowBrightness(getWindow());
        if (brightness < 200) {
            BrightnessUtils.setWindowBrightness(getWindow(), brightness);
        }
    }

    /**
     * 摄像头类型设置，可根据自己需求设置前置、后置及usb摄像头
     *
     * @param cameraImageSource
     */
    private void setCameraType(CameraImageSource cameraImageSource) {
        cameraImageSource.getCameraControl().setCameraFacing(ICameraControl.CAMERA_FACING_FRONT);
    }

    /**
     * 人脸检测启动
     */
    private void start() {
        RectF newDetectedRect = new RectF(0, 0, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
        cropProcessor.setDetectedRect(newDetectedRect);
        faceDetectManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        faceDetectManager.stop();
        mDetectStoped = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDetectStoped) {
            faceDetectManager.start();
            mDetectStoped = false;
        }
    }
}
