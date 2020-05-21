package com.android.dsly.biometrics;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.dsly.biometrics.databinding.ActivityMainBinding;
import com.android.dsly.biometrics.face.FaceAuthenticationActivity;
import com.android.dsly.biometrics.face.FaceCollectionActivity;
import com.android.dsly.biometrics.face.FaceDetectionActivity;
import com.android.dsly.biometrics.fingerprint.FingerprintCharacter;
import com.android.dsly.biometrics.fingerprint.FingerprintCharacterStepBuilder;
import com.android.dsly.biometrics.fingerprint.callback.FingerprintAuthenticatedCallback;
import com.blankj.utilcode.util.ActivityUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity implements FingerprintAuthenticatedCallback {
    private FingerprintCharacter fingerprintAuthenticatedCharacter;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //初始化指纹验证
        fingerprintAuthenticatedCharacter = FingerprintCharacterStepBuilder
                .newBuilder()
                .setKeystoreAlias("key1")
                .setFingerprintCallback(this)
                .build();

        mBinding.btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprintAuthenticatedCharacter.show(MainActivity.this);
            }
        });
        mBinding.btnFaceDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(FaceDetectionActivity.class);
            }
        });
        mBinding.btnFaceCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(FaceCollectionActivity.class);
            }
        });
        mBinding.btnFaceAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startActivity(FaceAuthenticationActivity.class);
            }
        });
    }

    /**
     * 指纹验证成功
     */
    @Override
    public void onFingerprintSucceed() {
        Toast.makeText(this, "指纹验证成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 指纹验证失败
     */
    @Override
    public void onFingerprintFailed() {
        Toast.makeText(this, "指纹验证失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * 取消验证
     */
    @Override
    public void onFingerprintCancel() {
        Toast.makeText(this, "取消指纹验证", Toast.LENGTH_SHORT).show();
    }

    /**
     * 没有录入指纹或者不支持指纹识别
     */
    @Override
    public void onNoEnrolledFingerprints() {
        Toast.makeText(this, "没有录入指纹锁", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNonsupportFingerprint() {
        Toast.makeText(this, "不支持指纹识别", Toast.LENGTH_SHORT).show();
    }
}