package com.htxtdshopping.htxtd.frame.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ThreadUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadManager {

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    public static void download(final String url, final String saveDir, final OnDownloadListener listener) {
        download(url, saveDir, null, listener);
    }

    public static void download(final String url, final String saveDir, String name, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                String fileName = TextUtils.isEmpty(name) ? getNameFromUrl(url) : name;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, fileName);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onProgress(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onSuccess(file);
                } catch (Exception e) {
                    File file = new File(savePath, fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    listener.onFail();
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private static String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        return downloadFile.getAbsolutePath();
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onSuccess(File file);

        /**
         * @param progress 下载进度
         */
        void onProgress(int progress);

        /**
         * 下载失败
         */
        void onFail();
    }

    public static class SimpleOnDownloadListener implements OnDownloadListener {

        @Override
        public void onSuccess(File file) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onUiSuccess(file);
                }
            });
        }

        @Override
        public void onProgress(int progress) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onUiProgress(progress);
                }
            });
        }

        @Override
        public void onFail() {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onUiFail();
                }
            });
        }

        public void onUiSuccess(File file) {


        }

        public void onUiProgress(int progress) {

        }

        public void onUiFail() {

        }
    }
}