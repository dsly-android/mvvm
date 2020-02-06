package com.htxtdshopping.htxtd.frame.widget.tablayout.utils;


import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.htxtdshopping.htxtd.frame.widget.tablayout.widget.MsgView;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * 未读消息提示View,显示小红点或者带有数字的红点:
 * 数字一位,圆
 * 数字两位,圆角矩形,圆角是高度的一半
 * 数字超过两位,显示99+
 */
public class UnreadMsgUtils {
    public static void show(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        ViewGroup.LayoutParams lp = msgView.getLayoutParams();
        msgView.setVisibility(View.VISIBLE);
        if (num <= 0) {
            //圆点,设置默认宽高
            msgView.setStrokeWidth(0);
            msgView.setText("");

            lp.width = AutoSizeUtils.dp2px(msgView.getContext(), 5);
            lp.height = AutoSizeUtils.dp2px(msgView.getContext(), 5);
            msgView.setLayoutParams(lp);
        } else {
            lp.height = AutoSizeUtils.dp2px(msgView.getContext(), 14);
            if (num > 0 && num < 10) {
                //圆
                lp.width = AutoSizeUtils.dp2px(msgView.getContext(), 14);
                msgView.setText(num + "");
            } else if (num > 9 && num < 100) {
                //圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding(AutoSizeUtils.dp2px(msgView.getContext(), 5), 0, AutoSizeUtils.dp2px(msgView.getContext(), 5), 0);
                msgView.setText(num + "");
            } else {
                //数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding(AutoSizeUtils.dp2px(msgView.getContext(), 5), 0, AutoSizeUtils.dp2px(msgView.getContext(), 5), 0);
                msgView.setText("99+");
            }
            msgView.setLayoutParams(lp);
        }
    }

    public static void setSize(MsgView rtv, int size) {
        if (rtv == null) {
            return;
        }
        ViewGroup.LayoutParams lp = rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }
}
