package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityBannerBinding;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.Arrays;
import java.util.List;

public class BannerActivity extends BaseActivity<ActivityBannerBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //修改指示器样式
//        updateBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        List<String> imgUrls = Arrays.asList(getResources().getStringArray(R.array.image_url));
        mBinding.bImages.setAdapter(new BannerImageAdapter<String>(imgUrls) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(Utils.getApp())
                        .load(data)
                        .into(holder.imageView);
            }
        }).addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(this))
                .setOnBannerListener(new OnBannerListener<String>() {
                    @Override
                    public void OnBannerClick(String data, int position) {
                        ToastUtils.showLong(position+"");
                    }
                });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
