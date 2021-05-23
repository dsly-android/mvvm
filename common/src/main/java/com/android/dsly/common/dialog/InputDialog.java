package com.android.dsly.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.dsly.common.R;
import com.blankj.utilcode.util.ScreenUtils;

import androidx.annotation.NonNull;

/**
 * Created by 陈志鹏 on 2018/3/22.
 */

public class InputDialog extends Dialog {

    public InputDialog(@NonNull Context context) {
        super(context, R.style.AppTheme_Dialog);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (ScreenUtils.getScreenWidth() * 0.8);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    public static class Builder {
        private Context mContext;
        private String mTitleStr;
        private String mHintStr;
        private String mCancelStr;
        private String mConfirmStr;
        private String mPromptStr;
        private String mContentStr;
        private boolean mPromptVisible;
        private boolean mConfirmVisible = true;
        private boolean mCancelable = true;
        private OnClickListener mListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitleStr(String titleStr) {
            this.mTitleStr = titleStr;
            return this;
        }

        public Builder setHintStr(String hintStr) {
            this.mHintStr = hintStr;
            return this;
        }

        public Builder setContentStr(String contentStr){
            this.mContentStr = contentStr;
            return this;
        }

        public Builder setCancelStr(String cancelStr) {
            this.mCancelStr = cancelStr;
            return this;
        }

        public Builder setConfirmStr(String confirmStr) {
            this.mConfirmStr = confirmStr;
            return this;
        }

        public Builder setPromptStr(String promptStr) {
            this.mPromptStr = promptStr;
            return this;
        }

        public Builder setPromptVisible(boolean promptVisible) {
            this.mPromptVisible = promptVisible;
            if (mPromptVisible) {
                mConfirmVisible = false;
            } else {
                mConfirmVisible = true;
            }
            return this;
        }

        public Builder setConfirmVisible(boolean confirmVisible) {
            this.mConfirmVisible = confirmVisible;
            if (mConfirmVisible) {
                mPromptVisible = false;
            } else {
                mPromptVisible = true;
            }
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder setOnClickListener(OnClickListener listener) {
            this.mListener = listener;
            return this;
        }

        public InputDialog create() {
            InputDialog dialog = new InputDialog(mContext);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(mCancelable);
            dialog.setContentView(R.layout.dialog_input);
            TextView tvTitle = dialog.findViewById(R.id.tv_title);
            EditText etInput = dialog.findViewById(R.id.et_input);
            LinearLayout llConfirm = dialog.findViewById(R.id.ll_confirm);
            TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
            TextView tvConfirm = dialog.findViewById(R.id.tv_confirm);
            TextView tvPrompt = dialog.findViewById(R.id.tv_prompt);
            tvPrompt.setVisibility(mPromptVisible ? View.VISIBLE : View.GONE);
            llConfirm.setVisibility(mConfirmVisible ? View.VISIBLE : View.GONE);
            if (!TextUtils.isEmpty(mTitleStr)) {
                tvTitle.setText(mTitleStr);
                tvTitle.setVisibility(View.VISIBLE);
            } else {
                tvTitle.setVisibility(View.GONE);
            }
            etInput.setHint(mHintStr);
            etInput.setText(mContentStr);
            if (!TextUtils.isEmpty(mPromptStr)) {
                tvPrompt.setText(mPromptStr);
            }
            if (!TextUtils.isEmpty(mCancelStr)) {
                tvCancel.setText(mCancelStr);
            }
            if (!TextUtils.isEmpty(mConfirmStr)) {
                tvConfirm.setText(mConfirmStr);
            }
            tvPrompt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onPromptClick(dialog, etInput.getText().toString().trim());
                    }
                }
            });
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onCancelClick(dialog);
                    }
                }
            });
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onConfirmClick(dialog, etInput.getText().toString().trim());
                    }
                }
            });
            return dialog;
        }
    }

    public abstract static class OnClickListener {
        public void onPromptClick(DialogInterface dialog, String content) {
            dialog.dismiss();
        }

        public void onCancelClick(DialogInterface dialog) {
            dialog.dismiss();
        }

        public void onConfirmClick(DialogInterface dialog, String content) {
            dialog.dismiss();
        }
    }
}
