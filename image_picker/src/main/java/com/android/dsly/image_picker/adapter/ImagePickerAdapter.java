package com.android.dsly.image_picker.adapter;

import android.widget.ImageView;

import com.android.dsly.common.utils.GlideUtils;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.activity.ImagePickerActivity;
import com.android.dsly.image_picker.local_data.ImageItem;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * @author 陈志鹏
 * @date 2018/12/10
 */
public class ImagePickerAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int CAMERA = 1;
    public static final int IMAGE = 2;
    private int mSelectMode;

    public ImagePickerAdapter(int selectMode) {
        super(new ArrayList<>());
        mSelectMode = selectMode;
        addItemType(CAMERA, R.layout.image_item_camera);
        addItemType(IMAGE, R.layout.image_item_image_picker);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        if (item.getItemType() == IMAGE) {
            ImageItem imageItem = (ImageItem) item;
            GlideUtils.loadImage(mContext,imageItem.path, helper.getView(R.id.iv_img));
            if (mSelectMode == ImagePickerActivity.MODE_AVATAR) {
                helper.setVisible(R.id.iv_check, false);
                helper.setVisible(R.id.v_cover, false);
            } else {
                helper.setVisible(R.id.iv_check, true);
                helper.setVisible(R.id.v_cover, imageItem.isChecked);
            }
            ImageView ivCheck = helper.getView(R.id.iv_check);
            ivCheck.setSelected(imageItem.isChecked);
            helper.addOnClickListener(R.id.iv_check);
        }
    }
}
