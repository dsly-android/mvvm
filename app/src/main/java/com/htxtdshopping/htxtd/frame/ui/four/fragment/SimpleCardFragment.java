package com.htxtdshopping.htxtd.frame.ui.four.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.android.dsly.common.base.BasePageLazyFragment;
import com.htxtdshopping.htxtd.frame.R;

import butterknife.BindView;

public class SimpleCardFragment extends BasePageLazyFragment {

    @BindView(R.id.tv_card_title)
    TextView mTvCardTitle;
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
        mTvCardTitle.setText(mTitle);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}