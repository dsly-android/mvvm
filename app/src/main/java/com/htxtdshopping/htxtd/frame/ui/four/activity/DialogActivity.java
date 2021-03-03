package com.htxtdshopping.htxtd.frame.ui.four.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.dialog.CenterActionItemDialog;
import com.android.dsly.common.dialog.ConfirmDialog;
import com.android.dsly.common.utils.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.dialog.TestDialog;

public class DialogActivity extends BaseActivity {

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

    public void confirm(View view) {
        new ConfirmDialog.Builder(this)
                .setContentStr("jlaf")
                .setOnClickListener(new ConfirmDialog.OnClickListener() {
                    @Override
                    public void onCancelClick(DialogInterface dialog) {
                        super.onCancelClick(dialog);
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog) {
                        super.onConfirmClick(dialog);
                    }
                }).create().show();
    }

    public void test(View view) {
        TestDialog testDialog = new TestDialog();
        testDialog.show(getSupportFragmentManager());
    }

    public void loading(View view){
        showLoading();
    }

    public void centerActionItem(View view){
        new CenterActionItemDialog.Builder()
                .addActionItem("飞机")
                .addActionItem("饿哦就反胃")
                .setOnItemClickListener(new CenterActionItemDialog.OnItemClickListener() {
                    @Override
                    public void onItemClick(String content, int pos) {
                        ToastUtils.showLong(content);
                    }
                })
                .show(getSupportFragmentManager());
    }
}
