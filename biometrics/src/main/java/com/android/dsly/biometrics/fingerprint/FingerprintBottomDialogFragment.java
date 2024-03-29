package com.android.dsly.biometrics.fingerprint;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.dsly.biometrics.R;
import com.android.dsly.biometrics.fingerprint.base.BaseDialog;
import com.android.dsly.biometrics.fingerprint.callback.FingerprintAuthenticatedCallback;

import androidx.annotation.Nullable;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.DialogFragment;

public class FingerprintBottomDialogFragment extends BaseDialog implements FingerprintAuthenticatedCallback {

    private ImageView mFingerprintIcon;
    private TextView mFingerprintStatus;
    private TextView mTVFingerprintCancel;

    private FingerprintManagerCompat.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private FingerprintAuthenticatedCallback mFingerprintAuthenticatedCallback;
    private FingerprintManagerCompat mFingerprintManager;

    public static FingerprintBottomDialogFragment newInstance() {
        Bundle args = new Bundle();
        FingerprintBottomDialogFragment fragment = new FingerprintBottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不要在重新创建活动时创建新的片段，例如方向更改。
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialog);
    }

    @Override
    public int getDialogWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fingerprint_bottom_dialog;
    }

    @Override
    protected void onInitFastData() {
        initViews();
        mFingerprintUiHelper = new FingerprintUiHelper(mFingerprintIcon, mFingerprintStatus, mFingerprintManager, this);
    }

    private void initViews() {
        mTVFingerprintCancel = getView().findViewById(R.id.fingerprint_cancel);
        mFingerprintStatus = getView().findViewById(R.id.fingerprint_status);
        mFingerprintIcon = getView().findViewById(R.id.fingerprint_icon);
        mTVFingerprintCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFingerprintCancel();
                dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mFingerprintUiHelper.startListening(mCryptoObject);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }

    /**
     * 设置使用指纹验证时要传入的crypto对象。
     */
    public void setCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    public void setFingerprintManager(FingerprintManagerCompat mFingerprintManager) {
        this.mFingerprintManager = mFingerprintManager;
    }

    public void setCallback(FingerprintAuthenticatedCallback mFingerprintAuthenticatedCallback) {
        this.mFingerprintAuthenticatedCallback = mFingerprintAuthenticatedCallback;
    }

    @Override
    public void onFingerprintSucceed() {
        if (null != mFingerprintAuthenticatedCallback) {
            mFingerprintAuthenticatedCallback.onFingerprintSucceed();
        }
        dismiss();
    }

    @Override
    public void onFingerprintFailed() {
        if (null != mFingerprintAuthenticatedCallback) {
            mFingerprintAuthenticatedCallback.onFingerprintFailed();
        }
    }

    @Override
    public void onFingerprintCancel() {
        if (null != mFingerprintAuthenticatedCallback) {
            mFingerprintAuthenticatedCallback.onFingerprintCancel();
        }
    }

    @Override
    public void onNoEnrolledFingerprints() {
        if (null != mFingerprintAuthenticatedCallback) {
            mFingerprintAuthenticatedCallback.onNoEnrolledFingerprints();
        }
    }

    @Override
    public void onNonsupportFingerprint() {
        if (null != mFingerprintAuthenticatedCallback) {
            mFingerprintAuthenticatedCallback.onNonsupportFingerprint();
        }
    }
}
