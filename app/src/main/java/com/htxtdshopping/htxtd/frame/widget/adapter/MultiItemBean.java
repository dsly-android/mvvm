package com.htxtdshopping.htxtd.frame.widget.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author 陈志鹏
 * @date 2019/1/16
 */
public class MultiItemBean implements MultiItemEntity {

    private int mItemType;

    public MultiItemBean(int itemType){
        mItemType = itemType;
    }

    public void setItemType(int itemType) {
        this.mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }
}
