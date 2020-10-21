package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        mBinding.etPhone.post(new Runnable() {
            @Override
            public void run() {
                KeyboardUtils.showSoftInput(mBinding.etPhone);
            }
        });
    }

    @Override
    public void initEvent() {
        mBinding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    return;
                }
                if (!s.toString().matches("[A-Za-z0-9]+")) {
                    String temp = s.toString();
                    ToastUtils.showLong("请输入数字或字母");
                    s.delete(temp.length() - 1, temp.length());
                    mBinding.etPassword.setSelection(s.length());
                }
            }
        });

        mBinding.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_showPW:
                if (mBinding.etPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mBinding.etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mBinding.tvShowPW.setText("隐藏密码");
                } else {
                    mBinding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mBinding.tvShowPW.setText("显示密码");
                }
                String pwd = mBinding.etPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd)) {
                    mBinding.etPassword.setSelection(pwd.length());
                }
                break;
            case R.id.btn_login:
                String phone = mBinding.etPhone.getText().toString();
                String password = mBinding.etPassword.getText().toString();
                if (!RegexUtils.isMobileExact(phone)) {
                    ToastUtils.showLong("请输入正确的手机号码");
                    return;
                }
                if (!password.matches("[A-Za-z0-9]+")) {
                    ToastUtils.showLong("请输入正确的密码");
                    return;
                }
                KeyboardUtils.hideSoftInput(this);
                break;
            case R.id.iv_wechat:

                break;
            case R.id.iv_retrieve_password:

                break;
            case R.id.iv_customer_service:

                break;
            default:
                break;
        }
    }

    @Override
    protected boolean isFitWindow() {
        return false;
    }
}
