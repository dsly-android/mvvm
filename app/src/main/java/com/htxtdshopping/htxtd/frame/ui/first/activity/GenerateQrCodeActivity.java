package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.ObjectUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityGenerateQrCodeBinding;
import com.mrd.common_service.service.IGenerateCodeService;

/**
 * @author chenzhipeng
 */
public class GenerateQrCodeActivity extends BaseFitsWindowActivity<ActivityGenerateQrCodeBinding, BaseViewModel> implements View.OnClickListener {

    @Autowired
    IGenerateCodeService mGenerateCodeService;

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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_generateQrCode:
                String code = mBinding.etCode.getText().toString();
                if (ObjectUtils.isEmpty(code)) {
                    return;
                }
                Bitmap bitmap = mGenerateCodeService.generateBarCode(code);
                mBinding.ivImg.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
    }
}