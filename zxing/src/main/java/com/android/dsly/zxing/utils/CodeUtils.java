package com.android.dsly.zxing.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Hashtable;

/**
 * 识别图片中的二维码
 *
 * @author 陈志鹏
 * @date 2020/9/14
 */
public class CodeUtils {

    public static String getResult(Bitmap bitmap) {
        String result = null;
        if (bitmap != null) {
            result = scanBitmap(bitmap);
        }
        if (!TextUtils.isEmpty(result)) {
            return result;
        }
        return null;
    }

    private static String scanBitmap(Bitmap bitmap) {
        Result result = scan(bitmap);
        if (result != null) {
            return recode(result.toString());
        } else {
            return null;
        }
    }

    private static String recode(String result) {
        String format = "";
        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(result);
            if (ISO) {
                format = new String(result.getBytes("ISO-8859-1"), "GB2312");
            } else {
                format = result;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return format;
    }

    private static Result scan(Bitmap bitmap) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        // 设置二维码内容的编码
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        Bitmap scanBitmap = Bitmap.createBitmap(bitmap);

        int px[] = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
        scanBitmap.getPixels(px, 0, scanBitmap.getWidth(), 0, 0,
                scanBitmap.getWidth(), scanBitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(
                scanBitmap.getWidth(), scanBitmap.getHeight(), px);
        BinaryBitmap tempBitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(tempBitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
