package com.android.dsly.biometrics.face;

import android.Manifest;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.dsly.biometrics.R;
import com.android.dsly.biometrics.databinding.ActivityFaceCollection2Binding;
import com.baidu.aip.FaceSDKManager;
import com.baidu.aip.ImageFrame;
import com.baidu.aip.face.CameraImageSource;
import com.baidu.aip.face.DetectRegionProcessor;
import com.baidu.aip.face.FaceDetectManager;
import com.baidu.aip.face.FaceFilter;
import com.baidu.aip.face.PreviewView;
import com.baidu.aip.face.camera.ICameraControl;
import com.baidu.aip.face.camera.PermissionCallback;
import com.baidu.idl.facesdk.FaceInfo;
import com.blankj.utilcode.util.BrightnessUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.databinding.DataBindingUtil;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.android.dsly.biometrics.face.utils.Base64RequestBody.readFile;

public class FaceScanActivity extends RxAppCompatActivity {

    private ActivityFaceCollection2Binding mBinding;
    private boolean mDetectStoped = false;
    private boolean mGoodDetect = false;
    private long mLastTipsTime = 0;
    private int mCurFaceId = -1;
    private static final double ANGLE = 15;
    private FaceDetectManager faceDetectManager;
    private DetectRegionProcessor cropProcessor = new DetectRegionProcessor();
    private boolean mSavedBmp = false;
    // 开始人脸检测
    private boolean mBeginDetect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_face_collection2);
        faceDetectManager = new FaceDetectManager(this);
        initView();
        initEvent();
        beginCapture();
    }

    private void beginCapture(){
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        visibleView();
                        mBeginDetect = true;
                    }
                });
    }

    private void initView() {
        CameraImageSource cameraImageSource = new CameraImageSource(this);
        cameraImageSource.setPreviewView(mBinding.previewView);
        faceDetectManager.setImageSource(cameraImageSource);
        faceDetectManager.setOnFaceDetectListener(new FaceDetectManager.OnFaceDetectListener() {
            @Override
            public void onDetectFace(final int retCode, FaceInfo[] infos, ImageFrame frame) {
                String str = "";
                if (retCode == 0) {
                    if (infos != null && infos[0] != null) {
                        FaceInfo info = infos[0];
                        boolean distance = false;
                        if (info != null && frame != null) {
                            if (info.mWidth >= (0.9 * frame.getWidth())) {
                                distance = false;
                                str = getResources().getString(R.string.detect_zoom_out);
                            } else if (info.mWidth <= 0.4 * frame.getWidth()) {
                                distance = false;
                                str = getResources().getString(R.string.detect_zoom_in);
                            } else {
                                distance = true;
                            }
                        }
                        boolean headUpDown;
                        if (info != null) {
                            if (info.headPose[0] >= ANGLE) {
                                headUpDown = false;
                                str = getResources().getString(R.string.detect_head_up);
                            } else if (info.headPose[0] <= -ANGLE) {
                                headUpDown = false;
                                str = getResources().getString(R.string.detect_head_down);
                            } else {
                                headUpDown = true;
                            }

                            boolean headLeftRight;
                            if (info.headPose[1] >= ANGLE) {
                                headLeftRight = false;
                                str = getResources().getString(R.string.detect_head_left);
                            } else if (info.headPose[1] <= -ANGLE) {
                                headLeftRight = false;
                                str = getResources().getString(R.string.detect_head_right);
                            } else {
                                headLeftRight = true;
                            }

                            if (distance && headUpDown && headLeftRight) {
                                mGoodDetect = true;
                            } else {
                                mGoodDetect = false;
                            }

                        }
                    }
                } else if (retCode == 1) {
                    str = getResources().getString(R.string.detect_head_up);
                } else if (retCode == 2) {
                    str = getResources().getString(R.string.detect_head_down);
                } else if (retCode == 3) {
                    str = getResources().getString(R.string.detect_head_left);
                } else if (retCode == 4) {
                    str = getResources().getString(R.string.detect_head_right);
                } else if (retCode == 5) {
                    str = getResources().getString(R.string.detect_low_light);
                } else if (retCode == 6) {
                    str = getResources().getString(R.string.detect_face_in);
                } else if (retCode == 7) {
                    str = getResources().getString(R.string.detect_face_in);
                } else if (retCode == 10) {
                    str = getResources().getString(R.string.detect_keep);
                } else if (retCode == 11) {
                    str = getResources().getString(R.string.detect_occ_right_eye);
                } else if (retCode == 12) {
                    str = getResources().getString(R.string.detect_occ_left_eye);
                } else if (retCode == 13) {
                    str = getResources().getString(R.string.detect_occ_nose);
                } else if (retCode == 14) {
                    str = getResources().getString(R.string.detect_occ_mouth);
                } else if (retCode == 15) {
                    str = getResources().getString(R.string.detect_right_contour);
                } else if (retCode == 16) {
                    str = getResources().getString(R.string.detect_left_contour);
                } else if (retCode == 17) {
                    str = getResources().getString(R.string.detect_chin_contour);
                }

                boolean faceChanged = true;
                if (infos != null && infos[0] != null) {
                    Log.d("DetectLogin", "face id is:" + infos[0].face_id);
                    if (infos[0].face_id == mCurFaceId) {
                        faceChanged = false;
                    } else {
                        faceChanged = true;
                    }
                    mCurFaceId = infos[0].face_id;
                }

                if (faceChanged) {
                    onRefreshSuccessView(false);
                }

                final int resultCode = retCode;
                if (!(mGoodDetect && retCode == 0)) {
                    if (faceChanged) {
                        onRefreshSuccessView(false);
                    }
                }

                if (retCode == 6 || retCode == 7 || retCode < 0) {
                    mBinding.rectView.processDrawState(true);
                } else {
                    mBinding.rectView.processDrawState(false);
                }

                final String curTips = str;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((System.currentTimeMillis() - mLastTipsTime) > 1000) {
                            mBinding.nameTextView.setText(curTips);
                            mLastTipsTime = System.currentTimeMillis();
                        }
                        if (mGoodDetect && resultCode == 0) {
                            mBinding.nameTextView.setText("");
                            onRefreshSuccessView(true);
                        }
                    }
                });

                if (infos == null) {
                    mGoodDetect = false;
                }
            }
        });
        faceDetectManager.setOnTrackListener(new FaceFilter.OnTrackListener() {
            @Override
            public void onTrack(FaceFilter.TrackedModel trackedModel) {
                if (trackedModel.meetCriteria() && mGoodDetect) {
                    // upload(trackedModel);
                    mGoodDetect = false;
                    if (!mSavedBmp && mBeginDetect) {
                        if (saveFaceBmp(trackedModel)) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }
            }
        });

        cameraImageSource.getCameraControl().setPermissionCallback(new PermissionCallback() {
            @Override
            public boolean onRequestPermission() {
                new RxPermissions(FaceScanActivity.this).request(Manifest.permission.CAMERA);
                return true;
            }
        });

        mBinding.rectView.post(new Runnable() {
            @Override
            public void run() {
                start();
            }
        });
        ICameraControl control = cameraImageSource.getCameraControl();
        control.setPreviewView(mBinding.previewView);
        // 设置检测裁剪处理器
        faceDetectManager.addPreProcessor(cropProcessor);

        if (ScreenUtils.isPortrait()) {
            mBinding.previewView.setScaleType(PreviewView.ScaleType.FIT_WIDTH);
        } else {
            mBinding.previewView.setScaleType(PreviewView.ScaleType.FIT_HEIGHT);
        }
        cameraImageSource.getCameraControl().setDisplayOrientation(ScreenUtils.getScreenRotation(this));
        //   previewView.getTextureView().setScaleX(-1);
        mBinding.successImage.post(new Runnable() {
            @Override
            public void run() {
                if ( mBinding.successImage.getTag() == null) {
                    Rect rect = mBinding.rectView.getFaceRoundRect();
                    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)  mBinding.successImage.getLayoutParams();
                    int w = (int) getResources().getDimension(R.dimen.success_width);
                    rlp.setMargins(
                            rect.centerX() - (w / 2),
                            rect.top - (w / 2),
                            0,
                            0);
                    mBinding.successImage.setLayoutParams(rlp);
                    mBinding.successImage.setTag("setlayout");
                }
                mBinding.successImage.setVisibility(View.GONE);
            }
        });

        init();
    }

    private void initEvent() {
        mBinding.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean saveFaceBmp(FaceFilter.TrackedModel model) {
        final Bitmap face = model.cropFace();
        String filePath = getExternalCacheDir().getAbsolutePath() + "/face/face1.png";
        if (face != null) {
            Log.d("save", "save bmp");
            FileUtils.createOrExistsFile(filePath);
            ImageUtils.save(face, filePath, Bitmap.CompressFormat.PNG);
        }
        final File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        boolean saved = false;
        try {
            byte[] buf = readFile(file);
            if (buf.length > 0) {
                saved = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!saved) {
            Log.d("fileSize", "file size >=-99");
        } else {
            mSavedBmp = true;
        }
        return saved;
    }

    private void visibleView() {
        mBinding.cameraLayout.setVisibility(View.INVISIBLE);
    }

    private void init() {
        FaceSDKManager.getInstance().getFaceTracker(this).set_min_face_size(200);
        FaceSDKManager.getInstance().getFaceTracker(this).set_isCheckQuality(true);
        // 该角度为商学，左右，偏头的角度的阀值，大于将无法检测出人脸，为了在1：n的时候分数高，注册尽量使用比较正的人脸，可自行条件角度
        FaceSDKManager.getInstance().getFaceTracker(this).set_eulur_angle_thr(15, 15, 15);
        FaceSDKManager.getInstance().getFaceTracker(this).set_isVerifyLive(true);
        FaceSDKManager.getInstance().getFaceTracker(this).set_notFace_thr(0.2f);
        FaceSDKManager.getInstance().getFaceTracker(this).set_occlu_thr(0.1f);

        initBrightness();
    }

    private void initBrightness() {
        int brightness = BrightnessUtils.getWindowBrightness(getWindow());
        if (brightness < 200) {
            BrightnessUtils.setWindowBrightness(getWindow(), 200);
        }
    }

    private void start() {
        int mScreenW = ScreenUtils.getScreenWidth();
        int mScreenH = ScreenUtils.getScreenHeight();

        Rect dRect = mBinding.rectView.getFaceRoundRect();

        //   RectF newDetectedRect = new RectF(detectedRect);
        int preGap = getResources().getDimensionPixelOffset(R.dimen.preview_margin);
        int w = getResources().getDimensionPixelOffset(R.dimen.detect_out);

        int orientation = getResources().getConfiguration().orientation;
        boolean isPortrait = (orientation == Configuration.ORIENTATION_PORTRAIT);
        if (isPortrait) {
            // 检测区域矩形宽度
            int rWidth = mScreenW - 2 * preGap;
            // 圆框宽度
            int dRectW = dRect.width();
            // 检测矩形和圆框偏移
            int h = (rWidth - dRectW) / 2;
            //  Log.d("liujinhui hi is:", " h is:" + h + "d is:" + (dRect.left - 150));
            int rLeft = w;
            int rRight = rWidth - w;
            int rTop = dRect.top - h - preGap + w;
            int rBottom = rTop + rWidth - w;

            //  Log.d("liujinhui", " rLeft is:" + rLeft + "rRight is:" + rRight + "rTop is:" + rTop + "rBottom is:" + rBottom);
            RectF newDetectedRect = new RectF(rLeft, rTop, rRight, rBottom);
            cropProcessor.setDetectedRect(newDetectedRect);
        } else {
            int rLeft = mScreenW / 2 - mScreenH / 2 + w;
            int rRight = mScreenW / 2 + mScreenH / 2 + w;
            int rTop = 0;
            int rBottom = mScreenH;

            RectF newDetectedRect = new RectF(rLeft, rTop, rRight, rBottom);
            cropProcessor.setDetectedRect(newDetectedRect);
        }

        faceDetectManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        faceDetectManager.stop();
        mDetectStoped = true;
        onRefreshSuccessView(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDetectStoped) {
            faceDetectManager.start();
            mDetectStoped = false;
        }
    }

    private void onRefreshSuccessView(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBinding.successImage.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}