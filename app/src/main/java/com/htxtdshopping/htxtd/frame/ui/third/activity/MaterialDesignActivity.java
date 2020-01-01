package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ActivityUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityMaterialDesignBinding;

public class MaterialDesignActivity extends BaseFitsWindowActivity<ActivityMaterialDesignBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_material_design;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.btnBottomSheet.setOnClickListener(this);
        mBinding.btnChip.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bottom_sheet:
                ActivityUtils.startActivity(BottomSheetActivity.class);
                break;
            case R.id.btn_chip:
                ActivityUtils.startActivity(ChipActivity.class);
                break;
            default:
                break;
        }
    }
}
