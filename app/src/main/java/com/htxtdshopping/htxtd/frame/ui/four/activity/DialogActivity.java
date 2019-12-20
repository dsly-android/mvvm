package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.dialog.ConfirmDialog;

public class DialogActivity extends BaseFitsWindowActivity {



    @Override
    public int getLayoutId() {
        return R.layout.activity_dialog;
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


    public void click(View view){
        new ConfirmDialog(this).show();
    }
}
