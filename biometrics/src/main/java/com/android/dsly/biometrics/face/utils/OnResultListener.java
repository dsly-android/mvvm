/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.android.dsly.biometrics.face.utils;


import com.android.dsly.biometrics.face.exception.FaceError;

public interface OnResultListener<T> {
    void onResult(T result);

    void onError(FaceError error);
}
