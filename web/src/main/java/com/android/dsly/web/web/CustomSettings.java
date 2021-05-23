package com.android.dsly.web.web;

import android.webkit.WebView;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;

/**
 * Created by cenxiaozhong on 2017/5/26.
 * source code  https://github.com/Justson/AgentWeb
 */
public class CustomSettings extends AbsAgentWebSettings {

    private AgentWeb mAgentWeb;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    @Override
    public IAgentWebSettings toSetting(WebView webView) {
        super.toSetting(webView);
        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        getWebSettings().setUseWideViewPort(true);
        // 缩放至屏幕的大小
        getWebSettings().setLoadWithOverviewMode(true);
        return this;
    }
}