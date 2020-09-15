package com.mrd.common_service.service;

import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author 陈志鹏
 * @date 2019-12-12
 */
public interface IGenerateCodeService extends IProvider {

    /**
     * 生成二维码
     * @param content
     * @return
     */
    Bitmap generateQrCode(String content);
    Bitmap generateQrCode(String content, int qrWidth, int qrHeight);
    Bitmap generateQrCode(String content, int qrWidth, int qrHeight, int backgroundColor, int codeColor);

    /**
     * 生成条形码
     * @param content
     * @return
     */
    Bitmap generateBarCode(String content);
    Bitmap generateBarCode(String content, int qrWidth, int qrHeight);
    Bitmap generateBarCode(String content, int qrWidth, int qrHeight, int backgroundColor, int codeColor);

    /**
     * 识别二维码和条形码
     */
    String decodeCode(Bitmap bitmap);
}