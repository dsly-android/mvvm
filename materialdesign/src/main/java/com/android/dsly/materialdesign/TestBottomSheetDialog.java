package com.android.dsly.materialdesign;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseBottomSheetDialog;
import com.android.dsly.materialdesign.databinding.DesignDialogSheetBottomBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2020-01-06
 */
public class TestBottomSheetDialog extends BaseBottomSheetDialog<DesignDialogSheetBottomBinding> {

    public TestBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.design_dialog_sheet_bottom;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        // 注意：这里要给layout的parent设置peekHeight，而不是在layout里给layout本身设置，下面设置背景色同理，坑爹！！！
        View mDialogSheetBottomView = (View) findViewById(R.id.nsv_scroll).getParent();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mDialogSheetBottomView);
        bottomSheetBehavior.setPeekHeight(AutoSizeUtils.dp2px(mContext,150));
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}