package com.android.dsly.common.dialog;

import android.content.Context;
import android.os.Bundle;

import com.android.dsly.common.R;
import com.android.dsly.common.base.BaseDialog;

import androidx.annotation.NonNull;

/**
 * @author 陈志鹏
 * @date 2018/10/25
 */
public class LoadingDialog extends BaseDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Translucent_Dialog);
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_loading;
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
    public void show() {
        super.showDefault();
    }
}
