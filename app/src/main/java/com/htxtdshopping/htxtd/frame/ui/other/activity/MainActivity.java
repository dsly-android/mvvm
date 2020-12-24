package com.htxtdshopping.htxtd.frame.ui.other.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.widget.pagerbottomtabstrip.NavigationController;
import com.android.dsly.common.widget.pagerbottomtabstrip.item.BaseTabItem;
import com.android.dsly.common.widget.pagerbottomtabstrip.item.SpecialTabItemView;
import com.android.dsly.common.widget.pagerbottomtabstrip.item.SpecialTabRoundItemView;
import com.android.dsly.common.widget.pagerbottomtabstrip.listener.SimpleTabItemSelectedListener;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FragmentUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityMainBinding;
import com.htxtdshopping.htxtd.frame.ui.center.fragment.CenterFragment;
import com.htxtdshopping.htxtd.frame.ui.first.fragment.FirstFragment;
import com.htxtdshopping.htxtd.frame.ui.four.fragment.FourFragment;
import com.htxtdshopping.htxtd.frame.ui.second.fragment.SecondFragment;
import com.htxtdshopping.htxtd.frame.ui.third.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * @author chenzhipeng
 */
public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> {

    private List<Fragment> mFragments;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private CenterFragment centerFragment;
    private ThirdFragment thirdFragment;
    private FourFragment fourFragment;
    private NavigationController mController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mController = mBinding.pnvTab.custom()
                .addItem(newItem(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, "框架"))
                .addItem(newItem(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, "第三方"))
                .addItem(newRoundItem(R.drawable.icon_center, R.drawable.icon_center))
                .addItem(newItem(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, "系统"))
                .addItem(newItem(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, "控件"))
                .build();

        if (savedInstanceState == null) {
            firstFragment = new FirstFragment();
            secondFragment = new SecondFragment();
            centerFragment = new CenterFragment();
            thirdFragment = new ThirdFragment();
            fourFragment = new FourFragment();
            mFragments = new ArrayList<>();
            mFragments.add(firstFragment);
            mFragments.add(secondFragment);
            mFragments.add(centerFragment);
            mFragments.add(thirdFragment);
            mFragments.add(fourFragment);
            FragmentUtils.add(getSupportFragmentManager(), mFragments, R.id.fl_container, 0);
        } else {
            firstFragment = (FirstFragment) FragmentUtils.findFragment(getSupportFragmentManager(), FirstFragment.class);
            secondFragment = (SecondFragment) FragmentUtils.findFragment(getSupportFragmentManager(), SecondFragment.class);
            centerFragment = (CenterFragment) FragmentUtils.findFragment(getSupportFragmentManager(),CenterFragment.class);
            thirdFragment = (ThirdFragment) FragmentUtils.findFragment(getSupportFragmentManager(), ThirdFragment.class);
            fourFragment = (FourFragment) FragmentUtils.findFragment(getSupportFragmentManager(), FourFragment.class);
            mFragments = new ArrayList<>();
            mFragments.add(firstFragment);
            mFragments.add(secondFragment);
            mFragments.add(centerFragment);
            mFragments.add(thirdFragment);
            mFragments.add(fourFragment);
        }
    }

    @Override
    public void initEvent() {
        mController.addSimpleTabItemSelectedListener(new SimpleTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                FragmentUtils.showHide(index, mFragments);
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTabItemView itemView = new SpecialTabItemView(this);
        itemView.initialize(drawable, checkedDrawable, text);
        itemView.setTextDefaultColor(Color.BLACK);
        itemView.setTextCheckedColor(getResources().getColor(R.color._81D8CF));
        itemView.setTextSize(12);
        itemView.setUnreadMsgTextSize(9);
        return itemView;
    }

    /**
     * 圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable) {
        SpecialTabRoundItemView itemView = new SpecialTabRoundItemView(this);
        itemView.initialize(drawable, checkedDrawable,"");
        return itemView;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    protected void initStatusBar() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    protected boolean isFitWindow() {
        return false;
    }
}