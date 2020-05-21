/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.android.dsly.biometrics.face.parser;


import com.android.dsly.biometrics.face.exception.FaceError;

/**
 * JSON解析
 * @param <T>
 */
public interface Parser<T> {
    T parse(String json) throws FaceError;
}
