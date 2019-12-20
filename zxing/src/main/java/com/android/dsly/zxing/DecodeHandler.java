/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.dsly.zxing;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

final class DecodeHandler extends Handler {

    private static final String TAG = DecodeHandler.class.getSimpleName();

    private final CaptureActivity activity;
    private final MultiFormatReader multiFormatReader;
    private boolean running = true;

    DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
        multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        if (message.what == R.id.zxing_decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.zxing_quit) {
            running = false;
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {
        if (ScreenUtils.isPortrait()) {
            // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
            }
            data = rotatedData;

            // 宽高也要调整
            int tmp = width;
            width = height;
            height = tmp;
        }

        long start = System.currentTimeMillis();
        Result rawResult = null;
        PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(activity, data, width, height);
        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }
        Handler handler = activity.getHandler();
        if (rawResult != null) {
            // Don't log the barcode contents for security.
            long end = System.currentTimeMillis();
            Log.e(TAG, "Found barcode in " + (end - start) + " ms");
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.zxing_decode_succeeded, rawResult);
                Bundle bundle = new Bundle();
                bundleThumbnail(source, bundle);
                message.setData(bundle);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Message message = Message.obtain(handler, R.id.zxing_decode_failed);
                message.sendToTarget();
            }
        }

        calculateLightBrightness(data, width, height);
    }

    private static void bundleThumbnail(PlanarYUVLuminanceSource source, Bundle bundle) {
        int[] pixels = source.renderThumbnail();
        int width = source.getThumbnailWidth();
        int height = source.getThumbnailHeight();
        Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
        bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width / source.getWidth());
    }

    /**
     * 上次记录的时间戳
     */
    long lastRecordTime = System.currentTimeMillis();
    /**
     * 上次记录的索引
     */
    int darkIndex = 0;
    /**
     * 一个历史记录的数组，255是代表亮度最大值
     */
    long[] darkList = new long[]{255, 255};
    /**
     * 扫描间隔
     */
    int waitScanTime = 250;
    /**
     * 亮度低的阀值
     */
    int darkValue = 60;

    /**
     * 计算光线亮度
     *
     * @param data
     * @param width
     * @param height
     */
    private void calculateLightBrightness(byte[] data, int width, int height) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRecordTime < waitScanTime) {
            return;
        }
        lastRecordTime = currentTime;

        //像素点的总亮度
        long pixelLightCount = 0L;
        //像素点的总数
        long pixelCount = width * height;
        //采集步长，因为没有必要每个像素点都采集，可以跨一段采集一个，减少计算负担，必须大于等于1。
        int step = 10;
        //data.length - allCount * 1.5f的目的是判断图像格式是不是YUV420格式，只有是这种格式才相等
        //因为int整形与float浮点直接比较会出问题，所以这么比
        if (Math.abs(data.length - pixelCount * 1.5f) < 0.00001f) {
            for (int i = 0; i < pixelCount; i += step) {
                //如果直接加是不行的，因为data[i]记录的是色值并不是数值，byte的范围是+127到—128，
                // 而亮度FFFFFF是11111111是-127，所以这里需要先转为无符号unsigned long参考Byte.toUnsignedLong()
                pixelLightCount += ((long) data[i]) & 0xffL;
            }
            //平均亮度
            long cameraLight = pixelLightCount / (pixelCount / step);
            //更新历史记录
            int lightSize = darkList.length;
            darkList[darkIndex = darkIndex % lightSize] = cameraLight;
            darkIndex++;
            boolean isDarkEnv = true;
            //判断在时间范围waitScanTime * lightSize内是不是亮度过暗
            for (int i = 0; i < lightSize; i++) {
                if (darkList[i] > darkValue) {
                    isDarkEnv = false;
                    break;
                }
            }
            Log.e(TAG, "摄像头环境亮度为 ： " + cameraLight);
            final boolean finalIsDarkEnv = isDarkEnv;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!activity.isFinishing()) {
                        activity.isDarkEnv(finalIsDarkEnv);
                    }
                }
            });
        }
    }
}
