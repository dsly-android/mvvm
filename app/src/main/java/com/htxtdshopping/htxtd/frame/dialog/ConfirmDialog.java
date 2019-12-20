package com.htxtdshopping.htxtd.frame.dialog;

import android.content.Context;
import android.os.Bundle;

import com.android.dsly.common.base.BaseDialog;
import com.htxtdshopping.htxtd.frame.R;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2019-11-06
 */
public class ConfirmDialog extends BaseDialog {

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_confirm;
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
}
