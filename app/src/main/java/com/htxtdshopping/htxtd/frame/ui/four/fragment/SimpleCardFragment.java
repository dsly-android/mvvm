package com.htxtdshopping.htxtd.frame.ui.four.fragment;

import android.os.Bundle;

import com.android.dsly.common.base.BasePageLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.FragmentSimpleCardBinding;

public class SimpleCardFragment extends BasePageLazyFragment<FragmentSimpleCardBinding, BaseViewModel> {

    private String mTitle;

    public static SimpleCardFragment getInstance(String title) {
        SimpleCardFragment sf = new SimpleCardFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_simple_card;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.tvCardTitle.setText(mTitle);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}