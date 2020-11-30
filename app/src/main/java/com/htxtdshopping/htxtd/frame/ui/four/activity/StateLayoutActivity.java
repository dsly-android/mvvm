package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityStateLayoutBinding;

public class StateLayoutActivity extends BaseActivity<ActivityStateLayoutBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_state_layout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initEvent() {
        mBinding.slState.setOnStateListener(state -> Log.i("MainActivity", "onStateChanged: state==" + state));
        mBinding.slState.setOnRetryListener(() -> {
            mBinding.slState.showLoading();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBinding.slState.showContent();
                    Toast.makeText(getApplicationContext(), "load data finish", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.network:
                mBinding.slState.showNetwork();
                break;
            case R.id.error:
                mBinding.slState.showError();
                break;
            case R.id.loading:
                mBinding.slState.showLoading();
                break;
            case R.id.empty:
                mBinding.slState.showNetwork();
                break;
            case R.id.content:
                mBinding.slState.showContent();
                break;
            default:
                break;
        }
    }
}
