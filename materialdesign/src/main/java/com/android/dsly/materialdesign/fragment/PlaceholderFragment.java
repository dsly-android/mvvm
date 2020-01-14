package com.android.dsly.materialdesign.fragment;

import android.os.Bundle;

import com.android.dsly.common.base.BasePageLazyFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignFragmentPlaceHolderBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends BasePageLazyFragment<DesignFragmentPlaceHolderBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_fragment_place_holder;
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
}