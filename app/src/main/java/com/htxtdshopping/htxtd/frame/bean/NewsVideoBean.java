package com.htxtdshopping.htxtd.frame.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.htxtdshopping.htxtd.frame.ui.first.adapter.RefreshAndLoadMoreAdapter;

/**
 * @author 陈志鹏
 * @date 2019/1/17
 */
public class NewsVideoBean implements MultiItemEntity {

    private String title;
    private String videoUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int getItemType() {
        return RefreshAndLoadMoreAdapter.TYPE_VIDEO;
    }
}
