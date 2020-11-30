package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ActivityUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivitySensorBinding;

public class SensorActivity extends BaseActivity<ActivitySensorBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_sensor;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_distance:
                ActivityUtils.startActivity(DistanceSensorActivity.class);
                break;
            default:
                break;
        }
    }
}
