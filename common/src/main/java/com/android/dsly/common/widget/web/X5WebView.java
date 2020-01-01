package com.android.dsly.common.widget.web;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.blankj.utilcode.util.NetworkUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import me.jessyan.autosize.AutoSize;

/**
 * @author 陈志鹏
 * @date 2018/12/5
 */
public class X5WebView extends WebView {

    public X5WebView(Context context) {
        this(context, null);
    }

    public X5WebView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public X5WebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new X5WebViewClient());
        initSetting();
    }

    private void initSetting() {
        WebSettings webSetting = this.getSettings();
        //关闭密码保存提醒
        webSetting.setSavePassword(false);
        // 设置与Js交互的权限
        webSetting.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSetting.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSetting.setLoadWithOverviewMode(true);
        //支持缩放，默认为true。是下面那个的前提。
        webSetting.setSupportZoom(true);
        ////设置内置的缩放控件。若为false，则该WebView不可缩放
        webSetting.setBuiltInZoomControls(true);
        //设置不可以访问文件
        webSetting.setAllowFileAccess(false);
        //禁止从 file url 中的 javascript 读取其它本地文件
        webSetting.setAllowFileAccessFromFileURLs(false);
        //不能通过file url加载的 Javascript 可以访问其他的源(包括http、https等源)
        webSetting.setAllowUniversalAccessFromFileURLs(false);
        //开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSetting.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //webview缓存
        if (NetworkUtils.isConnected()) {
            webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
    }

    /**
     * 解决webview偶尔适配失效的问题
     */
    @Override
    public void setOverScrollMode(int overScrollMode) {
        super.setOverScrollMode(overScrollMode);
        AutoSize.autoConvertDensityOfGlobal((Activity) getContext());
    }

    /**
     * 返回上一个网址
     */
    @Override
    public void goBack() {
        if (canGoBack()) {
            super.goBack();
        }
    }

    /**
     * 下一个网址
     */
    @Override
    public void goForward() {
        if (canGoForward()) {
            super.goForward();
        }
    }

    public void onDestroy() {
        clearHistory();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
        destroy();
    }
}