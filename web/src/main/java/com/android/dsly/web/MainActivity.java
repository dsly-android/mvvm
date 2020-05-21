package com.android.dsly.web;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.web.databinding.WebActivityMainBinding;

public class MainActivity extends BaseFitsWindowActivity<WebActivityMainBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.web_activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    public void click(View view){
        ARouter.getInstance()
                .build(RouterHub.WEB_WEB_ACTIVITY)
                .withString("url","https://www.baidu.com")
                .navigation();
    }
}
