package com.android.dsly.zxing.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

import androidx.annotation.NonNull;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 生成二维码的工具类
 *
 * @author 陈志鹏
 * @date 2018/10/30
 */
public class QrCodeUtils {

    /**
     * 获取建造者
     *
     * @param text 样式字符串文本
     */
    public static QrCodeUtils.Builder builder(@NonNull CharSequence text) {
        return new QrCodeUtils.Builder(text);
    }

    public static class Builder {

        private int backgroundColor = 0xffffffff;

        private int codeColor = 0xff000000;

        private int codeSide = AutoSizeUtils.pt2px(Utils.getApp(), 800);

        private CharSequence content;

        public Builder backColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder codeColor(int codeColor) {
            this.codeColor = codeColor;
            return this;
        }

        public Builder codeSide(int codeSide) {
            this.codeSide = codeSide;
            return this;
        }

        public Builder(@NonNull CharSequence text) {
            this.content = text;
        }

        public Bitmap into(ImageView imageView) {
            Bitmap bitmap = QrCodeUtils.encodeAsBitmap(content, codeSide, codeSide, backgroundColor, codeColor);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            return bitmap;
        }
    }

    //----------------------------------------------------------------------------------------------以下为生成二维码算法

    public static Bitmap encodeAsBitmap(CharSequence content, int qrWidth, int qrHeight, int backgroundColor, int codeColor) {
        Bitmap bitmap = null;
        try {
            // 判断URL合法性
            if (content == null || "".equals(content) || content.length() < 1) {
                return null;
            }
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //设置空白边距
            hints.put(EncodeHintType.MARGIN, 0);
            // 图像数据转换，使用了矩阵转换
            BitMatrix result;
            try {
                result = new MultiFormatWriter().encode(content + "", BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            } catch (IllegalArgumentException iae) {
                // Unsupported format
                return null;
            }
            int[] pixels = new int[qrWidth * qrHeight];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < qrHeight; y++) {
                int offset = y * qrWidth;
                for (int x = 0; x < qrWidth; x++) {
                    pixels[offset + x] = result.get(x, y) ? codeColor : backgroundColor;
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap encodeAsBitmap(CharSequence content, int qrWidth, int qrHeight) {
        return encodeAsBitmap(content, qrWidth, qrHeight, 0xffffffff, 0xff000000);
    }

    public static Bitmap encodeAsBitmap(CharSequence content) {
        return encodeAsBitmap(content, AutoSizeUtils.pt2px(Utils.getApp(), 800), AutoSizeUtils.pt2px(Utils.getApp(), 800));
    }

    //==============================================================================================二维码算法结束


    /**
     * @param content   需要转换的字符串
     * @param qrWidth  二维码的宽度
     * @param qrHeight 二维码的高度
     * @param ivCode   图片空间
     */
    public static void encodeAsBitmap(String content, int qrWidth, int qrHeight, ImageView ivCode) {
        ivCode.setImageBitmap(encodeAsBitmap(content, qrWidth, qrHeight));
    }

    /**
     * @param content 需要转换的字符串
     * @param ivCode 图片空间
     */
    public static void encodeAsBitmap(String content, ImageView ivCode) {
        ivCode.setImageBitmap(encodeAsBitmap(content));
    }
}
