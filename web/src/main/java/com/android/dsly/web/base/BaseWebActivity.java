package com.android.dsly.web.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.web.web.X5WebChromeClient;
import com.android.dsly.web.web.X5WebView;

import androidx.databinding.ViewDataBinding;
import me.jessyan.autosize.AutoSize;

/**
 * @author 陈志鹏
 * @date 2020/2/27
 */
public abstract class BaseWebActivity<VB extends ViewDataBinding, VM extends BaseViewModel> extends BaseFitsWindowActivity<VB, VM> {

    public static final int CODE_CHOOSE_FILE = 0;

    public abstract X5WebView getWebView();

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        //解决首次进入有webview的页面时适配失效的问题
        AutoSize.autoConvertDensityOfGlobal(this);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWebView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        X5WebChromeClient webChromeClient = (X5WebChromeClient) getWebView().getWebChromeClient();
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CHOOSE_FILE:
                    if (null != webChromeClient.getUploadFile()) {
                        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                        webChromeClient.getUploadFile().onReceiveValue(result);
                        webChromeClient.setUploadFile(null);
                    }
                    if (null != webChromeClient.getUploadFiles()) {
                        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                        webChromeClient.getUploadFiles().onReceiveValue(new Uri[]{result});
                        webChromeClient.setUploadFiles(null);
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != webChromeClient.getUploadFile()) {
                webChromeClient.getUploadFile().onReceiveValue(null);
                webChromeClient.setUploadFile(null);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getWebView().canGoBack()) {
            getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (getWebView() != null) {
            getWebView().onDestroy();
        }
        super.onDestroy();
    }
}
