package com.android.dsly.materialdesign.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.materialdesign.R;
import com.android.dsly.materialdesign.databinding.DesignActivityMdBinding;
import com.google.android.material.snackbar.Snackbar;

public class MdActivity extends BaseFitsWindowActivity<DesignActivityMdBinding, BaseViewModel> {

    @Override
    public int getLayoutId() {
        return R.layout.design_activity_md;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.cl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mBinding.cl1, "bbb", Snackbar.LENGTH_SHORT).show();
            }
        });
        mBinding.cl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mBinding.cl2, "ccc", Snackbar.LENGTH_SHORT)
                        .setAction("action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showShort("action");
                            }
                        })
                        .addCallback(new Snackbar.Callback() {

                            @Override
                            public void onShown(Snackbar sb) {
                                super.onShown(sb);
                                ToastUtils.showShort("onShown");
                            }

                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                ToastUtils.showShort("onDismissed");
                            }
                        }).show();
            }
        });
    }

    @Override
    public void initData() {

    }
}
