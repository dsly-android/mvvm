package com.htxtdshopping.htxtd.frame.ui.first.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityPictureSelectorBinding;
import com.htxtdshopping.htxtd.frame.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

public class PictureSelectorActivity extends BaseActivity<ActivityPictureSelectorBinding, BaseViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.activity_picture_selector;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.setOnClickListener(this);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_choose_one:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .theme(R.style.picture_white_style)
                        .isEnableCrop(true)
                        .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高，默认为true
                        .circleDimmedLayer(true)//圆形剪裁边框
                        .showCropGrid(false)
                        .showCropFrame(false)
                        .imageEngine(GlideEngine.createGlideEngine())
//                        .forResult(PictureConfig.CHOOSE_REQUEST);
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                ToastUtils.showLong(result.get(0).getCutPath());
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
            case R.id.btn_choose_more:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(9)
                        .theme(R.style.picture_white_style)
                        .imageEngine(GlideEngine.createGlideEngine())
//                        .forResult(PictureConfig.CHOOSE_REQUEST)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                for (LocalMedia media : result) {
                                    LogUtils.i("是否压缩:" + media.isCompressed());
                                    LogUtils.i("压缩:" + media.getCompressPath());
                                    LogUtils.i("原图:" + media.getPath());
                                    LogUtils.i("绝对路径:" + media.getRealPath());
                                    LogUtils.i("是否裁剪:" + media.isCut());
                                    LogUtils.i("裁剪:" + media.getCutPath());
                                    LogUtils.i("是否开启原图:" + media.isOriginal());
                                    LogUtils.i("原图路径:" + media.getOriginalPath());
                                    LogUtils.i("Android Q 特有Path:" + media.getAndroidQToPath());
                                    LogUtils.i("宽高: " + media.getWidth() + "x" + media.getHeight());
                                    LogUtils.i("Size: " + media.getSize());
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    ToastUtils.showLong(selectList.get(0).getCutPath());
                    break;
                default:
                    break;
            }
        }
    }
}