package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityViewPager2Binding;
import com.htxtdshopping.htxtd.frame.transformer.ScaleInTransformer;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.ViewPager2Adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPager2Activity extends BaseActivity<ActivityViewPager2Binding, BaseViewModel> {

    private ViewPager2Adapter mAdapter;
    private TabLayoutMediator mTabLayoutMediator;

    @Override
    public int getLayoutId() {
        return R.layout.activity_view_pager2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        //竖直滑动
//        mBinding.vpPage.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        //设置是否可以滑动
//        mBinding.vpPage.setUserInputEnabled(false);
        //当setOffscreenPageLimit被设置为OFFSCREEN_PAGE_LIMIT_DEFAULT(默认)时候会使用RecyclerView的缓存机制
        //offscreenPageLimit设置为1后会预加载进来一个页面，和ViewPager几乎是一样的效果
//        mBinding.vpPage.setOffscreenPageLimit(1);

        //一屏多页效果
        mBinding.vpPage.setOffscreenPageLimit(1);
        RecyclerView recyclerView = (RecyclerView) mBinding.vpPage.getChildAt(0);
        recyclerView.setPadding(20,0,20,0);
        recyclerView.setClipToPadding(false);

        //动画效果
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(10));
        transformer.addTransformer(new ScaleInTransformer());
        mBinding.vpPage.setPageTransformer(transformer);

        mAdapter = new ViewPager2Adapter();
        mBinding.vpPage.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mBinding.vpPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ToastUtils.showShort(position + "");
            }
        });
        mTabLayoutMediator = new TabLayoutMediator(mBinding.tlTab, mBinding.vpPage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(position + "");
            }
        });
        mTabLayoutMediator.attach();
    }

    @Override
    public void initData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add("data"+i);
        }
        mAdapter.setNewData(datas);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabLayoutMediator.detach();
    }
}
