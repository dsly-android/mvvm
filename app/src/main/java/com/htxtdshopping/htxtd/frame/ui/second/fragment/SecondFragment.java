package com.htxtdshopping.htxtd.frame.ui.second.fragment;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.FragmentSecondBinding;
import com.htxtdshopping.htxtd.frame.ui.second.activity.LoginAndShareActivity;
import com.htxtdshopping.htxtd.frame.ui.second.activity.OssActivity;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public class SecondFragment extends BaseLazyFragment<FragmentSecondBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_second;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mBinding.vBar, getResources().getColor(R.color._81D8CF));
    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_oss:
                ActivityUtils.startActivity(OssActivity.class);
                break;
            case R.id.btn_bugly:
                //检查是否有新的更新
                Beta.checkUpgrade();
                break;
            case R.id.btn_sophix:
                SophixManager.getInstance().queryAndLoadNewPatch();
                break;
            case R.id.btn_x5:
                ARouter.getInstance()
                        .build(RouterHub.WEB_WEB_ACTIVITY)
                        .withString("url","http://www.baidu.com")
                        .navigation();
                break;
            case R.id.btn_login_and_share:
                ActivityUtils.startActivity(LoginAndShareActivity.class);
                break;
            case R.id.btn_bugly_crash:
                CrashReport.testJavaCrash();
                break;
            default:
                break;
        }
    }
}
