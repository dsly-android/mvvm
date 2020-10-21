package com.android.dsly.materialdesign.activity;

import android.os.Bundle;

import com.android.dsly.common.base.AppFragmentPagerAdapter;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.fragment.PlaceholderFragment;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivityTabLayoutBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.LogUtils;

public class TabLayoutActivity extends BaseActivity<DesignActivityTabLayoutBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_tab_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        List<Class> class1 = new ArrayList<>();
        class1.add(PlaceholderFragment.class);
        class1.add(PlaceholderFragment.class);
        List<String> title1 = new ArrayList<>();
        title1.add("tab1");
        title1.add("tab2");
        mBinding.viewPager1.setAdapter(new AppFragmentPagerAdapter(getSupportFragmentManager(),class1, title1));
        mBinding.tab1.setupWithViewPager(mBinding.viewPager1);
        mBinding.tab1.getTabAt(1).select();
    }

    @Override
    public void initEvent() {
        mBinding.tab2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.e("onTabSelected:"+tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LogUtils.e("onTabUnselected:"+tab.getText());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LogUtils.e("onTabReselected:"+tab.getText());
            }
        });
    }

    @Override
    public void initData() {
        TabLayout.Tab addTab1 = mBinding.tab2.newTab().setText("addTab1").setIcon(R.mipmap.ic_launcher_round);
        mBinding.tab2.addTab(addTab1);
        TabLayout.Tab addTab2 = mBinding.tab2.newTab().setText("addTab2").setIcon(R.mipmap.ic_launcher_round);
        mBinding.tab2.addTab(addTab2);
        TabLayout.Tab addTab3 = mBinding.tab2.newTab().setText("addTab3").setIcon(R.mipmap.ic_launcher_round);
        mBinding.tab2.addTab(addTab3);
        TabLayout.Tab addTab4 = mBinding.tab2.newTab().setText("addTab4").setIcon(R.mipmap.ic_launcher_round);
        mBinding.tab2.addTab(addTab4);
        mBinding.tab2.getTabAt(3).select();
    }
}