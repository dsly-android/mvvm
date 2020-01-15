package com.htxtdshopping.htxtd.frame.ui.center.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.giflib.extension.GlideApp;
import com.bumptech.glide.Glide;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityGiflibBinding;

public class GiflibActivity extends BaseFitsWindowActivity<ActivityGiflibBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_giflib;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setActivity(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_giflib:
                GlideApp.with(GiflibActivity.this)
//                        .asGif2()
                        .load("file:///android_asset/demo.gif")
                        .skipMemoryCache(true)
                        .into(mBinding.ivShow);
                break;
            case R.id.btn_glide:
                Glide.with(GiflibActivity.this)
                        .load("file:///android_asset/demo.gif")
                        .into(mBinding.ivShow);
                break;
            default:
                break;
        }
    }
}
