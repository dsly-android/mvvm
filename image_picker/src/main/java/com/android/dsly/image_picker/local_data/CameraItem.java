package com.android.dsly.image_picker.local_data;

import com.android.dsly.image_picker.adapter.ImagePickerAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author 陈志鹏
 * @date 2018/12/11
 */
public class CameraItem implements MultiItemEntity {

    @Override
    public int getItemType() {
        return ImagePickerAdapter.CAMERA;
    }
}
