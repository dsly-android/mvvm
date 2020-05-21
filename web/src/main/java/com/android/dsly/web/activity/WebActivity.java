package com.android.dsly.web.activity;

import android.graphics.Bitmap;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.web.R;
import com.android.dsly.web.base.BaseWebActivity;
import com.android.dsly.web.databinding.WebActivityWebBinding;
import com.android.dsly.web.web.X5WebChromeClient;
import com.android.dsly.web.web.X5WebView;
import com.android.dsly.web.web.X5WebViewClient;
import com.tencent.smtt.sdk.WebView;

import static android.view.View.GONE;

/**
 * @author chenzhipeng
 */
@Route(path = RouterHub.WEB_WEB_ACTIVITY)
public class WebActivity extends BaseWebActivity<WebActivityWebBinding, BaseViewModel> {

    @Autowired
    String url;

    @Override
    public int getLayoutId() {
        return R.layout.web_activity_web;
    }

    @Override
    public void initEvent() {
        mBinding.wvWeb.setWebViewClient(new X5WebViewClient(){
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                mBinding.pbProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                mBinding.pbProgress.setVisibility(GONE);
            }
        });

        getWebView().setWebChromeClient(new X5WebChromeClient(this) {

            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                mBinding.pbProgress.setProgress(i);
            }
        });
    }

    @Override
    public void initData() {
        mBinding.wvWeb.loadUrl(url);
    }

    @Override
    public X5WebView getWebView() {
        return mBinding.wvWeb;
    }
}
