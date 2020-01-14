package com.android.dsly.image_picker.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.android.dsly.common.base.BaseFitsWindowActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.Constants;
import com.android.dsly.common.decoration.GridDividerItemDecoration;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.adapter.ImagePickerAdapter;
import com.android.dsly.image_picker.databinding.ImageActivityImagePickerBinding;
import com.android.dsly.image_picker.local_data.CameraItem;
import com.android.dsly.image_picker.local_data.ImageDataUtils;
import com.android.dsly.image_picker.local_data.ImageFolder;
import com.android.dsly.image_picker.local_data.ImageItem;
import com.android.dsly.image_picker.popup.ImageFolderPopupWindow;
import com.android.dsly.image_picker.utils.IntentUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * @author chenzhipeng
 */
public class ImagePickerActivity extends BaseFitsWindowActivity<ImageActivityImagePickerBinding, BaseViewModel> implements ImageDataUtils.OnImagesLoadedListener, ImageFolderPopupWindow.OnImageFolderSelectedListener, View.OnClickListener {

    public static final String RESULT_KEY_IMAGE_PATH = "result_key_image_path";
    private static final int CODE_CROP_IMAGE = 1;
    private static final int CODE_TAKE_PHOTO = 0;
    private static final int CODE_IMAGE_PREVIEW = 2;

    public static final String KEY_SELECT_MODE = "key_select_mode";
    //选择头像
    public static final int MODE_AVATAR = 1;
    //选择多张图片
    public static final int MODE_MULTIPLE = 2;

    //选择数量
    public static final String KEY_MAX_SELECT_NUM = "key_max_select_num";

    private int mSelectMode;
    private File mCameraTmpFile;
    private ImagePickerAdapter mAdapter;
    /**
     * 所有的图片文件夹
     */
    private List<ImageFolder> mImageFolders;
    private ImageFolderPopupWindow mFolderPopupWindow;
    //选中的图片
    private ArrayList<ImageItem> mSelectedImages;
    private int mMaxSelectNum;

    @Override
    public int getLayoutId() {
        return R.layout.image_activity_image_picker;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mSelectedImages = new ArrayList<>();

        mMaxSelectNum = getIntent().getIntExtra(KEY_MAX_SELECT_NUM, 1);
        mSelectMode = getIntent().getIntExtra(KEY_SELECT_MODE, MODE_AVATAR);
        if (mSelectMode == MODE_MULTIPLE) {
            mBinding.tvPreview.setVisibility(View.VISIBLE);
            mBinding.tbTitle.setRightTextVisible(View.VISIBLE);
            refreshView();
        }

        mBinding.rvContent.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.rvContent.addItemDecoration(new GridDividerItemDecoration(this, 10));
        mBinding.rvContent.setItemAnimator(null);
        mAdapter = new ImagePickerAdapter(mSelectMode);
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity entity = mAdapter.getData().get(position);
                int itemType = entity.getItemType();
                if (itemType == ImagePickerAdapter.CAMERA) {
                    requestPermission();
                } else {
                    ImageItem imageItem = (ImageItem) entity;
                    if (mSelectMode == MODE_AVATAR) {
                        IntentUtils.toImageCrop(ImagePickerActivity.this, imageItem.path, CODE_CROP_IMAGE);
                    } else {
                        IntentUtils.toImagePreview(ImagePickerActivity.this, mImageFolders.get(0).images, imageItem.path, mMaxSelectNum, CODE_IMAGE_PREVIEW);
                    }
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_check) {
                    ImageItem imageItem = (ImageItem) mAdapter.getItem(position);
                    if (imageItem.isChecked == false) {
                        if (mSelectedImages.size() >= mMaxSelectNum) {
                            ToastUtils.showLong("您最多只能选择" + mMaxSelectNum + "张图片");
                            return;
                        }
                    }
                    imageItem.isChecked = !imageItem.isChecked;
                    mAdapter.notifyItemChanged(position);
                    if (imageItem.isChecked == true) {
                        mSelectedImages.add(imageItem);
                    } else {
                        mSelectedImages.remove(imageItem);
                    }
                    refreshView();
                }
            }
        });
        mBinding.tbTitle.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult();
            }
        });
        mBinding.tbTitle.setOnTextBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBinding.clDir.setOnClickListener(this);
        mBinding.tvPreview.setOnClickListener(this);
        mBinding.rvContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Glide.with(ImagePickerActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Glide.with(ImagePickerActivity.this).pauseRequests();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {
        new ImageDataUtils(this, null, this);
    }

    private void refreshView() {
        if (mSelectedImages.size() == 0) {
            mBinding.tvPreview.setText("预览");
            mBinding.tbTitle.setRightTextString("完成");
            mBinding.tbTitle.setRightTextClickable(false);
            mBinding.tbTitle.setRightTextAlpha(0.5f);
        } else {
            mBinding.tvPreview.setText("预览(" + mSelectedImages.size() + ")");
            mBinding.tbTitle.setRightTextString("完成(" + mSelectedImages.size() + "/" + mMaxSelectNum + ")");
            mBinding.tbTitle.setRightTextClickable(true);
            mBinding.tbTitle.setRightTextAlpha(1);
        }
    }

    private void requestPermission() {
        new RxPermissions(this)
                .requestEachCombined(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 设置系统相机拍照后的输出路径
                            // 创建临时文件
                            if (SDCardUtils.isSDCardEnableByEnvironment()) {
                                mCameraTmpFile = new File(Constants.PATH_EXTERNAL_CACHE_IMAGE, "avatar.jpg");
                            } else {
                                mCameraTmpFile = new File(Constants.PATH_CACHE_IMAGE, "avatar.jpg");
                            }
                            IntentUtils.toCapture(ImagePickerActivity.this, mCameraTmpFile, CODE_TAKE_PHOTO);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtils.showLong("shouldShowRequestPermissionRationale");
                        } else {
                            ToastUtils.showLong("请到设置中开启相机权限");
                        }
                    }
                });
    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.mImageFolders = imageFolders;
        refreshData(0);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.cl_dir) {
            if (ObjectUtils.isEmpty(mImageFolders)) {
                return;
            }
            if (mFolderPopupWindow == null) {
                mFolderPopupWindow = new ImageFolderPopupWindow(this,
                        mBinding.rvContent.getHeight(), mImageFolders, this);
            }
            if (!mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.showAtLocation(mBinding.llFooter, Gravity.TOP, 0, mBinding.tbTitle.getBottom());
            } else {
                mFolderPopupWindow.dismiss();
            }
        } else if (i == R.id.tv_preview) {
            if (ObjectUtils.isEmpty(mImageFolders) || ObjectUtils.isEmpty(mImageFolders.get(0))) {
                ToastUtils.showLong("没有图片可以预览");
                return;
            }
            IntentUtils.toImagePreview(ImagePickerActivity.this, mImageFolders.get(0).images, mImageFolders.get(0).images.get(0).path, mMaxSelectNum, CODE_IMAGE_PREVIEW);
        }
    }

    @Override
    public void OnImageFolderSelected(int selectedPos) {
        refreshData(selectedPos);
        mBinding.tvDir.setText(mImageFolders.get(selectedPos).name);
    }

    private void refreshData(int selectedPos) {
        mAdapter.getData().clear();
        if (mSelectMode == MODE_AVATAR) {
            mAdapter.getData().add(new CameraItem());
        }
        if (mImageFolders.size() != 0) {
            mAdapter.addData(mImageFolders.get(selectedPos).images);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setResult() {
        ArrayList<String> imagePaths = new ArrayList<>();
        for (int i = 0; i < mSelectedImages.size(); i++) {
            imagePaths.add(mSelectedImages.get(i).path);
        }
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_IMAGE_PATH, imagePaths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    IntentUtils.toImageCrop(this, mCameraTmpFile.getAbsolutePath(), CODE_CROP_IMAGE);
                } else {
                    mCameraTmpFile.delete();
                }
                break;
            case CODE_CROP_IMAGE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String imagePath = data.getStringExtra(CropActivity.RESULT_KEY_IMAGE_PATH);
                Intent intent = new Intent();
                intent.putExtra(RESULT_KEY_IMAGE_PATH, imagePath);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case CODE_IMAGE_PREVIEW:
                if (resultCode != RESULT_OK) {
                    return;
                }
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePreviewActivity.RESULT_KEY_ALL_IMAGE);
                boolean isFinish = data.getBooleanExtra(ImagePreviewActivity.RESULT_KEY_FINISH, false);
                ArrayList<ImageItem> allImages = mImageFolders.get(0).images;
                for (int i = 0; i < allImages.size(); i++) {
                    allImages.get(i).isChecked = images.get(i).isChecked;
                }
                mSelectedImages.clear();
                for (int i = 0; i < allImages.size(); i++) {
                    if (allImages.get(i).isChecked) {
                        mSelectedImages.add(allImages.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
                refreshView();
                if (isFinish) {
                    setResult();
                }
                break;
            default:
                break;
        }
    }
}