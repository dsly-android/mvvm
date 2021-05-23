package com.android.dsly.web.activity;

import android.view.ViewGroup;
import android.webkit.WebView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.web.R;
import com.android.dsly.web.base.BaseWebActivity;
import com.android.dsly.web.databinding.WebActivityWebBinding;
import com.android.dsly.web.web.CustomSettings;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebChromeClient;

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

    }

    @Override
    public void initData() {
        super.initData();
        //加载新的url
//        mAgentWeb.getUrlLoader().loadUrl("");
        //获取网址
//        String url = mAgentWeb.getWebCreator().getWebView().getUrl();
    }

    public WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mBinding.tbTitle.setTitle(title);
            }
        };
    }

    @Override
    protected ViewGroup getAgentWebParent() {
        return mBinding.llParent;
    }

    @Override
    protected IAgentWebSettings getAgentWebSettings() {
        return new CustomSettings();
    }

    @Override
    public String getUrl() {
        return url;
    }
}