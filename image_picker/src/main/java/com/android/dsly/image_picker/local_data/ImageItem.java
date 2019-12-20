package com.android.dsly.image_picker.local_data;

import com.android.dsly.image_picker.adapter.ImagePickerAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author 陈志鹏
 * @date 2018/12/11
 */
public class ImageItem implements Serializable,MultiItemEntity {

    private static final long serialVersionUID = -8984188393608251715L;
    /**
     * 图片的名字
     */
    public String name;
    /**
     * 图片的路径
     */
    public String path;
    /**
     * 图片的大小
     */
    public long size;
    /**
     * 图片的宽度
     */
    public int width;
    /**
     * 图片的高度
     */
    public int height;
    /**
     * 图片的类型
     */
    public String mimeType;
    /**
     * 图片的创建时间
     */
    public long addTime;
    /**
     * 图片是否被选中
     */
    public boolean isChecked;

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ImageItem) {
            ImageItem item = (ImageItem) o;
            return this.path.equalsIgnoreCase(item.path) && this.addTime == item.addTime;
        }

        return super.equals(o);
    }

    @Override
    public int getItemType() {
        return ImagePickerAdapter.IMAGE;
    }
}