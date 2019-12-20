package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.blankj.utilcode.util.ObjectUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.mrd.common_service.service.GenerateCodeService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author chenzhipeng
 */
public class GenerateQrCodeActivity extends BaseFitsWindowActivity {

    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.iv_img)
    ImageView mIvImg;
    @Autowired
    GenerateCodeService mGenerateCodeService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_generate_qr_code;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.btn_generateQrCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_generateQrCode:
                String code = mEtCode.getText().toString();
                if (ObjectUtils.isEmpty(code)) {
                    return;
                }
                Bitmap bitmap = mGenerateCodeService.generateBarCode(code);
                mIvImg.setImageBitmap(bitmap);
                break;
            default:
                break;
        }
    }
}