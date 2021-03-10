package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.blankj.utilcode.util.ObjectUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityGenerateQrCodeBinding;
import com.htxtdshopping.htxtd.frame.ui.first.viewmodel.GenerateQrCodeViewModel;

import androidx.lifecycle.Observer;

/**
 * @author chenzhipeng
 */
public class GenerateQrCodeActivity extends BaseActivity<ActivityGenerateQrCodeBinding, GenerateQrCodeViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_generate_qr_code;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {
        mBinding.btnGenerateQrCode.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mViewModel.getGenerateQrCodeLiveData().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                mBinding.ivImg.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generateQrCode:
                String code = mBinding.etCode.getText().toString();
                if (ObjectUtils.isEmpty(code)) {
                    return;
                }
                mViewModel.generateQrCode(code);
                break;
            default:
                break;
        }
    }
}