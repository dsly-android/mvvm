package com.android.dsly.materialdesign;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseBottomSheetDialogFragment;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.materialdesign.databinding.DesignDialogSheetBottomBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2020-01-06
 */
public class TestBottomSheetDialog extends BaseBottomSheetDialogFragment<DesignDialogSheetBottomBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_dialog_sheet_bottom;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getView().post(new Runnable() {
            @Override
            public void run() {
                View parent = (View) getView().getParent();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
                BottomSheetBehavior behavior = (BottomSheetBehavior) params.getBehavior();
                //默认展开
//                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                //没展开时的高度
                behavior.setPeekHeight(AutoSizeUtils.dp2px(getContext(),150));
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public String getDialogTag() {
        return "TestBottomSheetDialog";
    }
}