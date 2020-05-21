package com.android.dsly.web.web;


import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author 陈志鹏
 * @date 2019-11-18
 */
public class X5WebViewClient extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        //忽略SSL证书错误检测
        //表示等待证书响应
        sslErrorHandler.proceed();
    }
}