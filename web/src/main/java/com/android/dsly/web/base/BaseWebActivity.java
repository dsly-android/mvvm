package com.android.dsly.web.base;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.web.R;
import com.android.dsly.web.web.BaseWebViewClient;
import com.blankj.utilcode.util.ConvertUtils;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebChromeClient;

import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;

/**
 * @author 陈志鹏
 * @date 2020/2/27
 */
public abstract class BaseWebActivity<VB extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity<VB, VM> {

    protected AgentWeb mAgentWeb;

    @Override
    public void initView(Bundle savedInstanceState) {
        //网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
//        mAgentWeb.getWebCreator().getWebView().setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
    public void initData() {
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(getAgentWebParent(), new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ContextCompat.getColor(this, R.color._81D8CF), ConvertUtils.dp2px(2))
                .setAgentWebWebSettings(getAgentWebSettings())
                .setWebChromeClient(getWebChromeClient())
                .setWebViewClient(new BaseWebViewClient())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)//严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                //添加baseurl的cookie
//                .additionalHttpHeader("http://android.myapp.com/", "cookie", "41bc7ddf04a26b91803f6b11817a5a1c")
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go(getUrl());
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    protected abstract String getUrl();

    protected IAgentWebSettings getAgentWebSettings() {
        return null;
    }

    protected abstract ViewGroup getAgentWebParent();

    protected WebChromeClient getWebChromeClient() {
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
