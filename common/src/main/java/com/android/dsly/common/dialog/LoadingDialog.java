package com.android.dsly.common.dialog;

import android.os.Bundle;
import android.view.WindowManager;

import com.android.dsly.common.R;
import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseDialogFragment;

/**
 * @author 陈志鹏
 * @date 2018/10/25
 */
public class LoadingDialog extends BaseDialogFragment {

    @Override
    public int getLayoutId() {
        return R.layout.dialog_loading;
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
    public void onBackPressed() {
        super.onBackPressed();
        if (getContext() instanceof BaseActivity){
            dismiss();
            ((BaseActivity) getContext()).finish();
        }
    }

    @Override
    public double getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getTheme() {
        return R.style.AppTheme_Translucent_Dialog;
    }

    @Override
    public String getDialogTag() {
        return "LoadingDialog";
    }
}
