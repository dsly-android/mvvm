package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.ActivityUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.utils.VoiceRecordManager;
import com.htxtdshopping.htxtd.frame.widget.VoiceLineView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * @author chenzhipeng
 */
public class VoiceRecordActivity extends BaseActivity {

    @BindView(R.id.vlv_wave)
    VoiceLineView mVlvWave;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    private VoiceRecordManager mManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice_record;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mManager = VoiceRecordManager.getInstance();
        mVlvWave.setPause();
    }

    @Override
    public void initEvent() {
        mManager.setOnAudioRecordListener(new VoiceRecordManager.OnAudioRecordListener() {
            @Override
            public void onStartRecord() {
                mVlvWave.setContinue();
            }

            @Override
            public void onAudioVolumeChanged(int volume, double db) {
                mVlvWave.setVolume((int) db);
            }

            @Override
            public void onPauseRecord() {
                mVlvWave.setPause();
            }

            @Override
            public void onResumeRecord() {
                mVlvWave.setContinue();
            }

            @Override
            public void onFinishRecord(int duration, File recordFile) {
                ToastUtils.showLong(duration + "  " + recordFile.getAbsolutePath());
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.stop();
        mManager.setOnAudioRecordListener(null);
    }

    public void start(View view) {
        new RxPermissions(this)
                .requestEach(Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            mManager.start();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtils.showLong("shouldShowRequestPermissionRationale");
                        } else {
                            ToastUtils.showLong("请到设置中开启相机权限");
                        }
                    }
                });
    }

    public void pause(View view) {
        mManager.pause();
    }

    public void resume(View view) {
        mManager.resume();
    }

    public void stop(View view) {
        mManager.stop();
    }

    public void record(View view) {
        ActivityUtils.startActivity(VoicePlayActivity.class);
    }
}
