package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityCommonViewBinding;
import com.htxtdshopping.htxtd.frame.widget.ShadowDrawable;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author chenzhipeng
 */
public class CommonViewActivity extends BaseActivity<ActivityCommonViewBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_view;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.sbTest1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBinding.sbTest2.toggle();
            }
        });
        mBinding.ivIndicator.setSelectedPosition(1);

        ShadowDrawable.setShadowDrawable(mBinding.tvShadow, Color.parseColor("#3D5AFE"),
                AutoSizeUtils.pt2px(this,16), Color.parseColor("#66000000"),
                AutoSizeUtils.pt2px(this,16), 0, 0);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
