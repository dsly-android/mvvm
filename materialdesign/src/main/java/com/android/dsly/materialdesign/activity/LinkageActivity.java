package com.android.dsly.materialdesign.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivityLinkageBinding;
import com.blankj.utilcode.util.ActivityUtils;

public class LinkageActivity extends BaseFitsWindowActivity<DesignActivityLinkageBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_linkage;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.btnScroll1.setOnClickListener(this);
        mBinding.btnScroll2.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_scroll1){
            ActivityUtils.startActivity(Linkage1Activity.class);
        }else if (id ==R.id.btn_scroll2){
            ActivityUtils.startActivity(Linkage2Activity.class);
        }
    }
}
