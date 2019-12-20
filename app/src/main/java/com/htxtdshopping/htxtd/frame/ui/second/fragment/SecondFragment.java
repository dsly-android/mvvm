package com.htxtdshopping.htxtd.frame.ui.second.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseLazyFragment;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.ui.second.activity.LoginAndShareActivity;
import com.htxtdshopping.htxtd.frame.ui.second.activity.OssActivity;
import com.htxtdshopping.htxtd.frame.ui.second.activity.WebActivity;
import com.taobao.sophix.SophixManager;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 陈志鹏
 * @date 2018/9/7
 */
public class SecondFragment extends BaseLazyFragment {

    @BindView(R.id.v_bar)
    View mVBar;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_second;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mVBar, getResources().getColor(R.color._81D8CF));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_oss, R.id.btn_bugly, R.id.btn_sophix, R.id.btn_x5, R.id.btn_login_and_share})
    public void onViewClicked(View view) {
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
                ActivityUtils.startActivity(WebActivity.class);
                break;
            case R.id.btn_login_and_share:
                ActivityUtils.startActivity(LoginAndShareActivity.class);
                break;
            default:
                break;
        }
    }
}
