package com.android.dsly.zxing.service;

import android.content.Context;
import android.graphics.Bitmap;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.zxing.utils.BarCodeUtils;
import com.android.dsly.zxing.utils.QrCodeUtils;
import com.mrd.common_service.service.GenerateCodeService;

/**
 * @author 陈志鹏
 * @date 2019-12-13
 */
@Route(path = RouterHub.ZXING_GENERATE_CODE_SERVICE)
public class GenerateCodeServiceImpl implements GenerateCodeService {

    @Override
    public Bitmap generateQrCode(String content) {
        return QrCodeUtils.encodeAsBitmap(content);
    }

    @Override
    public Bitmap generateQrCode(String content, int qrWidth, int qrHeight) {
        return QrCodeUtils.encodeAsBitmap(content, qrWidth, qrHeight);
    }

    @Override
    public Bitmap generateQrCode(String content, int qrWidth, int qrHeight, int backgroundColor, int codeColor) {
        return QrCodeUtils.encodeAsBitmap(content, qrWidth, qrHeight, backgroundColor, codeColor);
    }

    @Override
    public Bitmap generateBarCode(String content) {
        return BarCodeUtils.createBarCode(content);
    }

    @Override
    public Bitmap generateBarCode(String content, int qrWidth, int qrHeight) {
        return BarCodeUtils.createBarCode(content, qrWidth, qrHeight);
    }

    @Override
    public Bitmap generateBarCode(String content, int qrWidth, int qrHeight, int backgroundColor, int codeColor) {
        return BarCodeUtils.createBarCode(content, qrWidth, qrHeight, backgroundColor, codeColor);
    }

    @Override
    public void init(Context context) {

    }
}
