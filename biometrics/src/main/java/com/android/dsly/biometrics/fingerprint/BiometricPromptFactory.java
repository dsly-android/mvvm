package com.android.dsly.biometrics.fingerprint;

import android.os.Build;

import com.android.dsly.biometrics.fingerprint.base.BaseBiometricPrompt;
import com.android.dsly.biometrics.fingerprint.callback.FingerprintAuthenticatedCallback;

import javax.crypto.Cipher;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.fragment.app.FragmentActivity;

public class BiometricPromptFactory {
    private FragmentActivity activity;
    private Cipher cipher;
    private FingerprintManagerCompat mFingerprintManager;
    private FingerprintAuthenticatedCallback fingerprintCallback;
    private BaseBiometricPrompt prompt;

    public BiometricPromptFactory(FragmentActivity activity, Cipher cipher, FingerprintManagerCompat mFingerprintManager, FingerprintAuthenticatedCallback fingerprintCallback) {
        this.activity = activity;
        this.cipher = cipher;
        this.mFingerprintManager = mFingerprintManager;
        this.fingerprintCallback = fingerprintCallback;
    }


    public void execute(int version) {
        if (version >= Build.VERSION_CODES.P) {
            prompt = new BiometricPrompt29(activity, fingerprintCallback);
        } else if (version >= Build.VERSION_CODES.M) {
            prompt = new BiometricPrompt28(activity, cipher, mFingerprintManager, fingerprintCallback);
        } else {
            if (null != fingerprintCallback) {
                fingerprintCallback.onNonsupportFingerprint();
            }
            return;
        }
        prompt.show();
    }
}
