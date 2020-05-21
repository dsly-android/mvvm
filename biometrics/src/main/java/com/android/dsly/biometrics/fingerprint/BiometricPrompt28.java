package com.android.dsly.biometrics.fingerprint;


import com.android.dsly.biometrics.fingerprint.base.BaseBiometricPrompt;
import com.android.dsly.biometrics.fingerprint.callback.FingerprintAuthenticatedCallback;

import javax.crypto.Cipher;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.FragmentActivity;

public class BiometricPrompt28 implements BaseBiometricPrompt {
    private FragmentActivity activity;
    private Cipher cipher;
    private FingerprintManagerCompat mFingerprintManager;
    private FingerprintAuthenticatedCallback fingerprintCallback;

    public BiometricPrompt28(FragmentActivity activity, Cipher cipher, FingerprintManagerCompat mFingerprintManager, FingerprintAuthenticatedCallback fingerprintCallback) {
        this.activity = activity;
        this.cipher = cipher;
        this.mFingerprintManager = mFingerprintManager;
        this.fingerprintCallback = fingerprintCallback;
    }

    @Override
    public void show() {
        if (null == mFingerprintManager) {
            if (null != fingerprintCallback) {
                fingerprintCallback.onNoEnrolledFingerprints();
            }
            return;
        }
        FingerprintBottomDialogFragment fragment = FingerprintBottomDialogFragment.newInstance();
        fragment.setFingerprintManager(mFingerprintManager);
        fragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(cipher));
        fragment.setCallback(fingerprintCallback);
        fragment.show(activity.getSupportFragmentManager(), "FingerprintBottomDialogFragment");
    }
}
