package com.htxtdshopping.htxtd.frame.dialog;

import android.os.Bundle;

import com.android.dsly.common.base.BaseDialogFragment;
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
        setCancelable(false);
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
}
