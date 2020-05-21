package com.android.dsly.biometrics.fingerprint.callback;

import javax.crypto.Cipher;

import androidx.fragment.app.FragmentActivity;

/**
 * @author yangfeng
 * @create 2018/11/28 11:00
 * @Describe
 */
public interface FingerprintBaseCharacter {

    void show(FragmentActivity activity);

    void onCreateKey(String keyName, boolean invalidatedByBiometricEnrollment);

    boolean initCipher(Cipher cipher, String keyName);

}