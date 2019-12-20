package com.htxtdshopping.htxtd.frame.bean;


import com.htxtdshopping.htxtd.frame.widget.adapter.MultiItemBean;

/**
 * @author 陈志鹏
 * @date 2019/1/16
 */
public class ChannelBean extends MultiItemBean {

    private String channelName;
    private long channelId;

    public ChannelBean(int itemType) {
        super(itemType);
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }
}
