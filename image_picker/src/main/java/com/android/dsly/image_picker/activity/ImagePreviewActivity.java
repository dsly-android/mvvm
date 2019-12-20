package com.android.dsly.image_picker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.common.widget.TitleBar;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.R2;
import com.android.dsly.image_picker.adapter.ImagePageAdapter;
import com.android.dsly.image_picker.local_data.ImageItem;
import com.android.dsly.image_picker.widget.ZoomViewPager;
import com.blankj.utilcode.util.BarUtils;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewActivity extends BaseActivity {

    public static final String KEY_All_IMAGE = "key_all_image";
    public static final String KEY_SELECTED_PATH = "key_selected_path";
    //最大选择数量
    public static final String KEY_MAX_SELECT_NUM = "key_max_select_num";
    public static final String RESULT_KEY_ALL_IMAGE = "result_key_all_image";
    public static final String RESULT_KEY_FINISH = "result_key_finish";
    @BindView(R2.id.tb_title)
    TitleBar mTbTitle;
    @BindView(R2.id.tv_back)
    TextView mTvBack;
    @BindView(R2.id.tv_right)
    TextView mTvRight;
    @BindView(R2.id.zvp_page)
    ZoomViewPager mZvpPage;
    @BindView(R2.id.tv_check)
    TextView mTvCheck;
    @BindView(R2.id.fl_footer)
    FrameLayout mFlFooter;
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
        mTvRight.setVisibility(View.VISIBLE);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mTbTitle.getLayoutParams();
        params.height += BarUtils.getStatusBarHeight();
        mTbTitle.setLayoutParams(params);
        mTbTitle.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);

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
        mZvpPage.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mZvpPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mTvBack.setText((i + 1) + "/" + mAllImages.size());
                mTvCheck.setSelected(mAllImages.get(i).isChecked);
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
        mZvpPage.setCurrentItem(position);

        mTvBack.setText((position + 1) + "/" + mAllImages.size());
        mTvCheck.setSelected(mAllImages.get(position).isChecked);
    }

    @OnClick({R2.id.tv_back, R2.id.tv_right, R2.id.tv_check})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_back) {
            onBackPressed();
        } else if (i == R.id.tv_right) {
            setResult(true);
        } else if (i == R.id.tv_check) {
            ImageItem imageItem = mAllImages.get(mZvpPage.getCurrentItem());
            if (imageItem.isChecked == false) {
                if (mSelectedImages.size() >= mMaxSelectNum) {
                    ToastUtils.showLong("您最多只能选择" + mMaxSelectNum + "张图片");
                    return;
                }
            }
            imageItem.isChecked = !imageItem.isChecked;
            mTvCheck.setSelected(imageItem.isChecked);
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
            mTvRight.setText("完成");
            mTvRight.setClickable(false);
            mTvRight.setAlpha(0.5f);
        } else {
            mTvRight.setText("完成(" + mSelectedImages.size() + "/" + mMaxSelectNum + ")");
            mTvRight.setClickable(true);
            mTvRight.setAlpha(1);
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
        mTbTitle.animate().translationY(-mTbTitle.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        mFlFooter.animate().alpha(0.0f).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showStatusBar() {
        mBarStatus = true;
        mTbTitle.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFlFooter.animate().alpha(1.0f).setInterpolator(new DecelerateInterpolator(2));
    }
}
