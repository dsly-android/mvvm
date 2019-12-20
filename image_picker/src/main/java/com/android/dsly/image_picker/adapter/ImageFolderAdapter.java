package com.android.dsly.image_picker.adapter;

import com.android.dsly.common.utils.GlideUtils;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.local_data.ImageFolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author 陈志鹏
 * @date 2018/12/11
 */
public class ImageFolderAdapter extends BaseQuickAdapter<ImageFolder, BaseViewHolder> {

    private int mSelectedPos;

    public ImageFolderAdapter() {
        super(R.layout.image_item_image_folder);
    }

    public void setSelectedPos(int selectedPos) {
        this.mSelectedPos = selectedPos;
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageFolder item) {
        int position = helper.getLayoutPosition();
        helper.setText(R.id.tv_folder_name, item.name);
        helper.setText(R.id.tv_image_count, mContext.getString(R.string.image_folder_image_count, item.images.size()));
        GlideUtils.loadImage(mContext, item.cover.path, helper.getView(R.id.iv_cover));
        if (mSelectedPos == position) {
            helper.setVisible(R.id.iv_folder_check, true);
        } else {
            helper.setVisible(R.id.iv_folder_check, false);
        }
    }
}
