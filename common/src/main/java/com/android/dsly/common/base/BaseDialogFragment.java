package com.android.dsly.common.base;

import android.content.DialogInterface;

import androidx.fragment.app.DialogFragment;

/**
 * @author 陈志鹏
 * @date 2018/10/26
 */
public class BaseDialogFragment extends DialogFragment {

    /**
     * 监听弹出窗是否被取消
     */
    private DialogInterface.OnDismissListener mDismissListener;

    public BaseDialogFragment setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.mDismissListener = dismissListener;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDismissListener != null) {
            mDismissListener.onDismiss(dialog);
        }
        super.onDismiss(dialog);
    }
}