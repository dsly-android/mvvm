package com.android.dsly.materialdesign;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RouterHub;
import com.android.dsly.materialdesign.activity.BottomSheetActivity;
import com.android.dsly.materialdesign.activity.CardViewActivity;
import com.android.dsly.materialdesign.activity.ChipActivity;
import com.android.dsly.materialdesign.activity.DrawerLayoutActivity;
import com.android.dsly.materialdesign.activity.LinkageActivity;
import com.android.dsly.materialdesign.activity.MaterialViewsActivity;
import com.android.dsly.materialdesign.activity.MdActivity;
import com.android.dsly.materialdesign.activity.SearchViewActivity;
import com.android.dsly.materialdesign.activity.TabLayoutActivity;
import com.android.dsly.materialdesign.databinding.DesignActivityMaterialDesignBinding;
import com.blankj.utilcode.util.ActivityUtils;

@Route(path = RouterHub.DESIGN_MATERIAL_DESIGN_ACTIVITY)
public class MaterialDesignActivity extends BaseActivity<DesignActivityMaterialDesignBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_material_design;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_cardview) {
            ActivityUtils.startActivity(CardViewActivity.class);
        } else if (i == R.id.btn_search) {
            ActivityUtils.startActivity(SearchViewActivity.class);
        } else if (i == R.id.btn_bottom_sheet) {
            ActivityUtils.startActivity(BottomSheetActivity.class);
        } else if (i == R.id.btn_chip) {
            ActivityUtils.startActivity(ChipActivity.class);
        } else if (i == R.id.btn_md) {
            ActivityUtils.startActivity(MdActivity.class);
        } else if (i == R.id.btn_drawer) {
            ActivityUtils.startActivity(DrawerLayoutActivity.class);
        } else if (i == R.id.btn_tab) {
            ActivityUtils.startActivity(TabLayoutActivity.class);
        } else if (i == R.id.btn_linkage) {
            ActivityUtils.startActivity(LinkageActivity.class);
        } else if (i == R.id.btn_views) {
            ActivityUtils.startActivity(MaterialViewsActivity.class);
        }
    }
}
