package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.tv_showPW)
    TextView mTvShowPW;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void initEvent() {
        mEtPassword.addTextChangedListener(new TextWatcher() {
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
                    mEtPassword.setSelection(s.length());
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_showPW, R.id.btn_login, R.id.iv_wechat, R.id.iv_retrieve_password, R.id.iv_customer_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_showPW:
                if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mTvShowPW.setText("隐藏密码");
                } else {
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mTvShowPW.setText("显示密码");
                }
                String pwd = mEtPassword.getText().toString();
                if (!TextUtils.isEmpty(pwd)){
                    mEtPassword.setSelection(pwd.length());
                }
                break;
            case R.id.btn_login:
                String phone = mEtPhone.getText().toString();
                String password = mEtPassword.getText().toString();
                if (!RegexUtils.isMobileExact(phone)){
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
}
