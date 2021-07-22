package com.android.dsly.web;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.web.activity.JsWebActivity;
import com.android.dsly.web.databinding.WebActivityMainBinding;
import com.blankj.utilcode.util.ActivityUtils;

public class MainActivity extends BaseActivity<WebActivityMainBinding, BaseViewModel> {

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

    public void click(View view) {
        ARouter.getInstance()
                .build(RouterHub.WEB_WEB_ACTIVITY)
                //正常网址
                .withString("url","http://39.108.85.192:4000/h5/#/")
//                .withString("url","https://www.pku.edu.cn")
                //屏幕自适应
//                .withString("url","https://www.eeafj.cn")
                //下载文件
//                .withString("url","http://android.myapp.com/")
                //上传文件
//                .withString("url","file:///android_asset/upload_file/uploadfile.html")
                //js上传文件
//                .withString("url","file:///android_asset/upload_file/jsuploadfile.html")
                //视频播放
//                .withString("url", "https://www.youku.com")
                //电话、短信、邮件
//                .withString("url","file:///android_asset/sms/sms.html")
                //地图
//                .withString("url", "https://map.baidu.com/mobile/webapp/index/index/#index/index/foo=bar/vt=map")
                .navigation();
    }

    public void jsCommunicate(View view) {
        ActivityUtils.startActivity(JsWebActivity.class);
    }
}
