package com.android.dsly.web.web;

import android.content.Intent;
import android.net.Uri;

import com.android.dsly.common.base.BaseActivity;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;

import static com.android.dsly.web.base.BaseWebActivity.CODE_CHOOSE_FILE;

/**
 * 上传文件要重写的方法
 * @author 陈志鹏
 * @date 2020/2/27
 */
public class X5WebChromeClient extends WebChromeClient {

    /**
     * 上传文件
     */
    private ValueCallback<Uri> uploadFile;
    private ValueCallback<Uri[]> uploadFiles;

    private BaseActivity mActivity;

    public X5WebChromeClient(BaseActivity activity){
        mActivity = activity;
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadFile) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android  > 4.1.1
    @Override
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        this.uploadFile = uploadFile;
        openFileChooseProcess();
    }

    // For Android  >= 5.0
    @Override
    public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        this.uploadFiles = filePathCallback;
        openFileChooseProcess();
        return true;
    }

    /**
     * 选择文件
     */
    private void openFileChooseProcess() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        mActivity.startActivityForResult(Intent.createChooser(i, "文件选择"), CODE_CHOOSE_FILE);
    }

    public void setUploadFile(ValueCallback<Uri> uploadFile) {
        this.uploadFile = uploadFile;
    }

    public ValueCallback<Uri> getUploadFile() {
        return uploadFile;
    }

    public void setUploadFiles(ValueCallback<Uri[]> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public ValueCallback<Uri[]> getUploadFiles() {
        return uploadFiles;
    }
}