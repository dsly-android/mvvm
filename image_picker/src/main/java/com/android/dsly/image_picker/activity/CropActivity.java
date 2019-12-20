package com.android.dsly.image_picker.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.constant.Constants;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.common.widget.TitleBar;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.R2;
import com.android.dsly.image_picker.widget.cropimage.CropImageLayout;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * @author chenzhipeng
 */
public class CropActivity extends BaseFitsWindowActivity {

    public static final String KEY_IMAGE_PATH = "key_image_path";
    public static final String RESULT_KEY_IMAGE_PATH = "result_key_image_path";
    @BindView(R2.id.cil_crop)
    CropImageLayout mCilCrop;
    @BindView(R2.id.tb_title)
    TitleBar mTbTitle;
    private File mTmpFile;

    @Override
    public int getLayoutId() {
        return R.layout.image_activity_crop;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTbTitle.setRightTextString("完成");
    }

    @Override
    public void initEvent() {
        mTbTitle.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SDCardUtils.isSDCardEnableByEnvironment()) {
                    mTmpFile = new File(Constants.PATH_EXTERNAL_CACHE_IMAGE, "crop.jpg");
                } else {
                    mTmpFile = new File(Constants.PATH_CACHE_IMAGE, "crop.jpg");
                }
                Bitmap bitmap = mCilCrop.crop();
                boolean isSucceed = ImageUtils.save(bitmap, mTmpFile, Bitmap.CompressFormat.PNG, true);
                if (isSucceed) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY_IMAGE_PATH, mTmpFile.getAbsolutePath());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showLong("图片裁剪失败");
                    finish();
                }
            }
        });
    }

    @Override
    public void initData() {
        loadImage();
    }

    private void loadImage() {
        String imagePath = getIntent().getStringExtra(KEY_IMAGE_PATH);
        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(imagePath).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = ImageUtils.drawable2Bitmap(resource);
                mCilCrop.init(CropActivity.this, bitmap);
            }
        });
    }
}
