package com.htxtdshopping.htxtd.frame.ui.center.fragment;


import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.BarUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.FragmentCenterBinding;
import com.htxtdshopping.htxtd.frame.ui.center.activity.CustomViewActivity;
import com.htxtdshopping.htxtd.frame.ui.center.activity.GiflibActivity;
import com.htxtdshopping.htxtd.frame.ui.center.activity.LinearActivity;
import com.htxtdshopping.htxtd.frame.ui.center.activity.SlideCloseActivity;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterFragment extends BaseLazyFragment<FragmentCenterBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_center;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(mBinding.vBar, getResources().getColor(R.color._81D8CF));

    }

    @Override
    public void initEvent() {
        mBinding.btnLinear.setOnClickListener(this);
        mBinding.btnSlideClose.setOnClickListener(this);
        mBinding.btnView.setOnClickListener(this);
        mBinding.btnGiflib.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_linear:
                ActivityUtils.startActivity(LinearActivity.class);
                break;
            case R.id.btn_slide_close:
                ActivityUtils.startActivity(SlideCloseActivity.class);
                break;
            case R.id.btn_view:
                ActivityUtils.startActivity(CustomViewActivity.class);
                break;
            case R.id.btn_giflib:
                ActivityUtils.startActivity(GiflibActivity.class);
                break;
            default:
                break;
        }
    }
}
