package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.htxtdshopping.htxtd.frame.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class BottomSheetActivity extends BaseFitsWindowActivity {

    @BindView(R.id.nsv_scroll)
    NestedScrollView mNsvScroll;
    private BottomSheetDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bottom_sheet;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_sheet_bottom);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        //自带的bottomsheet
        BottomSheetBehavior behavior = BottomSheetBehavior.from(mNsvScroll);
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
        //dialog的bottomsheet
        // 注意：这里要给layout的parent设置peekHeight，而不是在layout里给layout本身设置，下面设置背景色同理，坑爹！！！
        View mDialogSheetBottomView = (View) dialog.findViewById(R.id.nsv_scroll).getParent();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(mDialogSheetBottomView);
        bottomSheetBehavior.setPeekHeight(AutoSizeUtils.pt2px(this,300));
        mDialogSheetBottomView.setBackgroundColor(ContextCompat.getColor(this,android.R.color.white));
    }

    @OnClick({R.id.btn_bottom_sheet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bottom_sheet:
                dialog.show();
                break;
            default:
                break;
        }
    }
}
