package com.android.dsly.zxing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.zxing.camera.CameraConfigurationManager;
import com.android.dsly.zxing.camera.CameraManager;
import com.android.dsly.zxing.databinding.ZxingActivityCaptureBinding;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.google.zxing.Result;

import java.io.IOException;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

/**
 * @author 陈志鹏
 * @date 2018/10/29
 */
@Route(path = RouterHub.ZXING_CAPTURE_ACTIVITY)
public final class CaptureActivity extends BaseActivity<ZxingActivityCaptureBinding, BaseViewModel> implements SurfaceHolder.Callback, View.OnClickListener {

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public int getLayoutId() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return R.layout.zxing_activity_capture;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.transparent));

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mBinding.ivLightSwitch.getLayoutParams();
        params.bottomMargin = (ScreenUtils.getAppScreenHeight() - mBinding.vvScanArea.getFrameHeight()) / 2 + 10;

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
    }

    @Override
    public void initEvent() {
        mBinding.ivLightSwitch.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_light_switch) {
            if (cameraManager.isFlashlightOn()) {
                cameraManager.setTorch(false);
                mBinding.ivLightSwitch.setImageResource(R.drawable.zxing_torch_off);
            } else {
                cameraManager.setTorch(true);
                mBinding.ivLightSwitch.setImageResource(R.drawable.zxing_torch_on);
            }
        }
    }

    public void isDarkEnv(boolean isDarkEnv) {
        if (isDarkEnv || cameraManager.isFlashlightOn()) {
            mBinding.ivLightSwitch.setVisibility(View.VISIBLE);
        } else {
            mBinding.ivLightSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        beepManager.updatePrefs();

        inactivityTimer.onResume();

        SurfaceHolder surfaceHolder = mBinding.svPreview.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.sv_preview);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            LogUtils.e("*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param barcode A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result result, Bitmap barcode) {
        //如果返回上一个页面则要注释掉下面一行
//        inactivityTimer.onActivity();

        String text = result.getText();
        LogUtils.e("二维码/条形码 扫描结果：" + text);
        Intent intent = new Intent();
        intent.putExtra(RouterHub.ZXING_RESULT_KEY_SCAN_RESULT, text);
        setResult(RESULT_OK, intent);
        finish();

        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager.playBeepSoundAndVibrate();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            LogUtils.e("initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager);
            }
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("很遗憾，Android 相机出现问题。你可能需要重启设备。");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 根据布局中的二维码的区域计算出对应的相机界面上的区域
     *
     * @param configManager
     * @return
     */
    public Rect getScanArea(CameraConfigurationManager configManager) {
        int cameraWidth = 0;
        int cameraHeight = 0;
        if (ScreenUtils.isLandscape()) {
            cameraWidth = configManager.getCameraResolution().x;
            cameraHeight = configManager.getCameraResolution().y;
        } else {
            cameraWidth = configManager.getCameraResolution().y;
            cameraHeight = configManager.getCameraResolution().x;
        }

        /** 获取布局中扫描框的位置信息 */
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getAppScreenHeight();
        int cropLeft = (screenWidth - mBinding.vvScanArea.getFrameWidth()) / 2;
        int cropTop = (screenHeight - mBinding.vvScanArea.getFrameHeight()) / 2;

        int cropWidth = mBinding.vvScanArea.getFrameWidth();
        int cropHeight = mBinding.vvScanArea.getFrameHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = mBinding.clParent.getWidth();
        int containerHeight = mBinding.clParent.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        return new Rect(x, y, width + x, height + y);
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.zxing_restart_preview, delayMS);
        }
    }
}