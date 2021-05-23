package com.htxtdshopping.htxtd.frame.ui.third.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.dsly.common.base.BaseActivity;
import com.android.dsly.common.base.BaseViewModel;
import com.android.dsly.common.constant.RequestCode;
import com.android.dsly.common.decoration.GridDividerItemDecoration;
import com.android.dsly.common.utils.IntentUtils;
import com.android.dsly.common.utils.ToastUtils;
import com.android.dsly.image_picker.activity.ImagePickerActivity;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.UriUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.databinding.ActivityChangeAvatarBinding;
import com.htxtdshopping.htxtd.frame.ui.third.adapter.ChangeAvatarAdapter;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import io.reactivex.functions.Consumer;

/**
 * @author chenzhipeng
 */
public class ChangeAvatarActivity extends BaseActivity<ActivityChangeAvatarBinding, BaseViewModel> implements View.OnClickListener {

    private static final int CODE_SINGLE_PICKER = 0;
    private static final int CODE_MULTIPLE_PICKER = 1;
    private ChangeAvatarAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_avatar;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mBinding.rvContent.setLayoutManager(new GridLayoutManager(this, 4));
        mBinding.rvContent.addItemDecoration(new GridDividerItemDecoration(this, 10));
        mAdapter = new ChangeAvatarAdapter();
        mBinding.rvContent.setAdapter(mAdapter);
    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                new RxPermissions(ChangeAvatarActivity.this)
                        .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    Intent intent = new Intent(ChangeAvatarActivity.this, ImagePickerActivity.class);
                                    intent.putExtra(ImagePickerActivity.KEY_SELECT_MODE, ImagePickerActivity.MODE_MULTIPLE);
                                    intent.putExtra(ImagePickerActivity.KEY_MAX_SELECT_NUM, 9);
                                    startActivityForResult(intent, CODE_MULTIPLE_PICKER);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    ToastUtils.showLong("此功能需要文件权限");
                                } else {
                                    ToastUtils.showLong("请到设置中开启文件权限");
                                }
                            }
                        });
            }
        });
        mBinding.ivAvatar.setOnClickListener(this);
        mBinding.tvPickFile.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mAdapter.addData("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CODE_SINGLE_PICKER:
                String imagePath = data.getStringExtra(ImagePickerActivity.RESULT_KEY_IMAGE_PATH);
                mBinding.ivAvatar.setImageBitmap(ImageUtils.getBitmap(imagePath));
                break;
            case CODE_MULTIPLE_PICKER:
                List<String> imagePaths = (List<String>) data.getSerializableExtra(ImagePickerActivity.RESULT_KEY_IMAGE_PATH);
                mAdapter.setNewData(imagePaths);
                break;
            case RequestCode.REQUEST_CHOOSE_FILE:
                File file = UriUtils.uri2File(data.getData());
                if (file == null) {
                    ToastUtils.showLong("选择文件错误");
                    return;
                }
                ToastUtils.showLong(file.getAbsolutePath());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                new RxPermissions(this)
                        .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    Intent intent = new Intent(ChangeAvatarActivity.this, ImagePickerActivity.class);
                                    intent.putExtra(ImagePickerActivity.KEY_SELECT_MODE, ImagePickerActivity.MODE_AVATAR);
                                    startActivityForResult(intent, CODE_SINGLE_PICKER);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    ToastUtils.showLong("此功能需要文件权限");
                                } else {
                                    ToastUtils.showLong("请到设置中开启文件权限");
                                }
                            }
                        });
                break;
            case R.id.tv_pick_file:
                Intent pickFileIntent = IntentUtils.getPickFileIntent();
                startActivityForResult(pickFileIntent, RequestCode.REQUEST_CHOOSE_FILE);
                break;
            default:
                break;
        }
    }
}
