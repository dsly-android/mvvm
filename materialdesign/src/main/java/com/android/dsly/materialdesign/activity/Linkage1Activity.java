package com.android.dsly.materialdesign.activity;

import android.os.Bundle;

import com.android.dsly.common.base.AppFragmentPagerAdapter;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.fragment.LinkageFragment;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivityLinkage1Binding;

import java.util.ArrayList;
import java.util.List;

public class Linkage1Activity extends BaseActivity<DesignActivityLinkage1Binding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_linkage1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        List<Class> fragments = new ArrayList<>();
        fragments.add(LinkageFragment.class);
        fragments.add(LinkageFragment.class);
        fragments.add(LinkageFragment.class);

        List<String> names = new ArrayList<>();
        names.add("title1");
        names.add("title2");
        names.add("title3");
        AppFragmentPagerAdapter adapter = new AppFragmentPagerAdapter(getSupportFragmentManager(), fragments, names);
        mBinding.vpPage.setAdapter(adapter);

        mBinding.tlTab.setupWithViewPager(mBinding.vpPage);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
