package com.android.dsly.web.web;


import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.just.agentweb.WebViewClient;

/**
 * @author 陈志鹏
 * @date 2019-11-18
 */
public class BaseWebViewClient extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        //忽略SSL证书错误检测
        //表示等待证书响应
        sslErrorHandler.proceed();
    }
}