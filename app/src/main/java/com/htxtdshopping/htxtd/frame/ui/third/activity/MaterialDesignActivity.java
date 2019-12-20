package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.blankj.utilcode.util.ActivityUtils;
import com.htxtdshopping.htxtd.frame.R;

import butterknife.OnClick;

public class MaterialDesignActivity extends BaseFitsWindowActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_material_design;
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

    @OnClick({R.id.btn_bottom_sheet,R.id.btn_chip})
    public void onViewClicked(View view) {
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
