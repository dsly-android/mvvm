package com.android.dsly.materialdesign.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivityMaterialViewsBinding;
import com.google.android.material.chip.ChipGroup;

import me.jessyan.autosize.utils.LogUtils;

public class MaterialViewsActivity extends BaseFitsWindowActivity<DesignActivityMaterialViewsBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_material_views;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

            }
        });
        mBinding.chipGroup.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {

            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });
        mBinding.chipClose.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("aaa");
            }
        });
        mBinding.smSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.e(""+isChecked);
            }
        });
    }

    @Override
    public void initData() {

    }
}
