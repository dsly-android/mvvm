package com.android.dsly.image_picker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.adapter.ImagePageAdapter;
import com.android.dsly.image_picker.databinding.ImageActivityImagePreviewBinding;
import com.android.dsly.image_picker.local_data.ImageItem;
import com.blankj.utilcode.util.BarUtils;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class ImagePreviewActivity extends BaseActivity<ImageActivityImagePreviewBinding, BaseViewModel> implements View.OnClickListener {

    public static final String KEY_All_IMAGE = "key_all_image";
    public static final String KEY_SELECTED_PATH = "key_selected_path";
    //最大选择数量
    public static final String KEY_MAX_SELECT_NUM = "key_max_select_num";
    public static final String RESULT_KEY_ALL_IMAGE = "result_key_all_image";
    public static final String RESULT_KEY_FINISH = "result_key_finish";

    private ArrayList<ImageItem> mAllImages;
    private String mSelectedPath;
    private ImagePageAdapter mAdapter;
    //状态栏是否显示
    private boolean mBarStatus = true;
    //选中的图片
    private ArrayList<ImageItem> mSelectedImages;
    //最大选择数量
    private int mMaxSelectNum;

    @Override
    public int getLayoutId() {
        return R.layout.image_activity_image_preview;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.transparent));
        mBinding.tbTitle.setRightTextVisible(View.VISIBLE);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mBinding.tbTitle.getLayoutParams();
        params.height += BarUtils.getStatusBarHeight();
        mBinding.tbTitle.setLayoutParams(params);
        mBinding.tbTitle.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);

        mAllImages = (ArrayList<ImageItem>) getIntent().getSerializableExtra(KEY_All_IMAGE);
        mSelectedPath = getIntent().getStringExtra(KEY_SELECTED_PATH);
        mMaxSelectNum = getIntent().getIntExtra(KEY_MAX_SELECT_NUM, 1);

        mSelectedImages = new ArrayList<>();
        for (int i = 0; i < mAllImages.size(); i++) {
            ImageItem imageItem = mAllImages.get(i);
            if (imageItem.isChecked) {
                mSelectedImages.add(imageItem);
            }
        }

        refreshFinish();

        mAdapter = new ImagePageAdapter(this, mAllImages);
        mBinding.zvpPage.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mBinding.zvpPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mBinding.tbTitle.setBackTextString(((i + 1) + "/" + mAllImages.size()));
                mBinding.tvCheck.setSelected(mAllImages.get(i).isChecked);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mAdapter.setOnItemClickListener(new ImagePageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mBarStatus) {
                    hideStatusBar();
                } else {
                    showStatusBar();
                }
            }
        });
        mBinding.tbTitle.setOnTextBackClickListener(this);
        mBinding.tbTitle.setOnRightTextClickListener(this);
        mBinding.tvCheck.setOnClickListener(this);
    }

    @Override
    public void initData() {
        int position = 0;
        for (int i = 0; i < mAllImages.size(); i++) {
            if (mSelectedPath.equals(mAllImages.get(i).path)) {
                position = i;
                break;
            }
        }
        mBinding.zvpPage.setCurrentItem(position);

        mBinding.tbTitle.setBackTextString((position + 1) + "/" + mAllImages.size());
        mBinding.tvCheck.setSelected(mAllImages.get(position).isChecked);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.tv_back) {
            onBackPressed();
        } else if (i == R.id.tv_right) {
            setResult(true);
        } else if (i == R.id.tv_check) {
            ImageItem imageItem = mAllImages.get(mBinding.zvpPage.getCurrentItem());
            if (imageItem.isChecked == false) {
                if (mSelectedImages.size() >= mMaxSelectNum) {
                    ToastUtils.showLong("您最多只能选择" + mMaxSelectNum + "张图片");
                    return;
                }
            }
            imageItem.isChecked = !imageItem.isChecked;
            mBinding.tvCheck.setSelected(imageItem.isChecked);
            if (imageItem.isChecked == true) {
                mSelectedImages.add(imageItem);
            } else {
                mSelectedImages.remove(imageItem);
            }
            refreshFinish();
        }
    }

    private void refreshFinish() {
        if (mSelectedImages.size() == 0) {
            mBinding.tbTitle.setRightTextString("完成");
            mBinding.tbTitle.setRightTextClickable(false);
            mBinding.tbTitle.setRightTextAlpha(0.5f);
        } else {
            mBinding.tbTitle.setRightTextString("完成(" + mSelectedImages.size() + "/" + mMaxSelectNum + ")");
            mBinding.tbTitle.setRightTextClickable(true);
            mBinding.tbTitle.setRightTextAlpha(1);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(false);
    }

    public void setResult(boolean isFinish) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_ALL_IMAGE, mAllImages);
        intent.putExtra(RESULT_KEY_FINISH, isFinish);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void hideStatusBar() {
        mBarStatus = false;
        mBinding.tbTitle.animate().translationY(- mBinding.tbTitle.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        mBinding.flFooter.animate().alpha(0.0f).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showStatusBar() {
        mBarStatus = true;
        mBinding.tbTitle.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mBinding.flFooter.animate().alpha(1.0f).setInterpolator(new DecelerateInterpolator(2));
    }
}
