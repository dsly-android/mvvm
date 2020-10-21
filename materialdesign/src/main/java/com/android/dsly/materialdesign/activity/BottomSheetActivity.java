package com.android.dsly.materialdesign.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.TestBottomSheetDialog;
import com.android.dsly.materialdesign.databinding.DesignActivityBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;

public class BottomSheetActivity extends BaseActivity<DesignActivityBottomSheetBinding, BaseViewModel> implements View.OnClickListener {

    private TestBottomSheetDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_bottom_sheet;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        dialog = new TestBottomSheetDialog();
    }

    @Override
    public void initEvent() {
        mBinding.btnBottomSheet.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //自带的bottomsheet
        BottomSheetBehavior behavior = BottomSheetBehavior.from(mBinding.flParent);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_bottom_sheet) {
            dialog.show(getSupportFragmentManager());
        }
    }
}
