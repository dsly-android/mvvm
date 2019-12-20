package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.utils.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.loader.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.Arrays;

import butterknife.BindView;

public class BannerActivity extends BaseActivity {

    @BindView(R.id.b_images)
    Banner mBImages;

    @Override
    public int getLayoutId() {
        return R.layout.activity_banner;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //修改指示器样式
//        updateBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBImages.setImages(Arrays.asList(getResources().getStringArray(R.array.image_url)))
                .setImageLoader(new GlideImageLoader())
                //banner加载动画
//                .setBannerAnimation(DefaultTransformer.class)
                //指示器显示位置
//                .setIndicatorGravity(Gravity.CENTER)
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        ToastUtils.showLong(position+"");
                    }
                })
                .start();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBImages.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBImages.stopAutoPlay();
    }
}
