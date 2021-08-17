package com.android.dsly.push;

/**
 * @author 陈志鹏
 * @date 2021/7/9
 */
public class AliPushEvent {
    private String chatType;
    private Long otherId;

    public AliPushEvent(String chatType, Long otherId) {
        this.chatType = chatType;
        this.otherId = otherId;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public Long getOtherId() {
        return otherId;
    }

    public void setOtherId(Long otherId) {
        this.otherId = otherId;
    }
}
