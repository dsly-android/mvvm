package com.htxtdshopping.htxtd.frame.dialog;

import android.os.Bundle;

import com.android.dsly.common.base.BaseDialogFragment;
import com.blankj.utilcode.util.KeyboardUtils;
import com.htxtdshopping.htxtd.frame.R;

/**
 * @author 陈志鹏
 * @date 2020/9/18
 */
public class TestDialog extends BaseDialogFragment {

    @Override
    public int getLayoutId() {
        return R.layout.dialog_test;
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

    @Override
    public String getDialogTag() {
        return "TestDialog";
    }

    /**
     * dialog消失后隐藏软键盘
     * 点击对话框外部不会使dialog消失，需要设置：
     * getDialog().setCanceledOnTouchOutside(false);
     */
    @Override
    public void dismiss() {
        KeyboardUtils.hideSoftInput(getDialog().getCurrentFocus());
        super.dismiss();
    }
}