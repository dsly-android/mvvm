package com.android.dsly.image_picker.adapter;

import com.android.dsly.common.utils.GlideUtils;
import com.android.dsly.image_picker.R;
import com.android.dsly.image_picker.databinding.ImageItemImageFolderBinding;
import com.android.dsly.image_picker.local_data.ImageFolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import androidx.databinding.DataBindingUtil;

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
        ImageItemImageFolderBinding binding = helper.getBinding();
        binding.setData(item);

        GlideUtils.loadImage(getContext(), item.cover.path, helper.getView(R.id.iv_cover));

        int position = helper.getLayoutPosition();
        if (mSelectedPos == position) {
            helper.setVisible(R.id.iv_folder_check, true);
        } else {
            helper.setVisible(R.id.iv_folder_check, false);
        }
    }

    @Override
    protected void onItemViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        super.onItemViewHolderCreated(viewHolder, viewType);
        DataBindingUtil.bind(viewHolder.itemView);
    }
}
