package com.htxtdshopping.htxtd.frame.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.htxtdshopping.htxtd.frame.ui.first.adapter.RefreshAndLoadMoreAdapter;

/**
 * @author 陈志鹏
 * @date 2019/1/17
 */
public class NewsTextBean implements MultiItemEntity {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return RefreshAndLoadMoreAdapter.TYPE_TEXT;
    }
}
