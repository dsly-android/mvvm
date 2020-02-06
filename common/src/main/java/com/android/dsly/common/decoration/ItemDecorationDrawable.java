package com.android.dsly.common.decoration;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ShapeDrawable;

import me.jessyan.autosize.utils.AutoSizeUtils;

/**
 * @author 陈志鹏
 * @date 2018/11/5
 */
public class ItemDecorationDrawable extends ShapeDrawable {

    public ItemDecorationDrawable(Context context, int widthOrHeight) {
        this(context, widthOrHeight, android.R.color.transparent);
    }

    public ItemDecorationDrawable(Context context, int widthOrHeight, int color) {
        this(context, widthOrHeight, widthOrHeight, color);
    }

    public ItemDecorationDrawable(Context context, int width, int height, int color) {
        setIntrinsicWidth(AutoSizeUtils.dp2px(context, width));
        setIntrinsicHeight(AutoSizeUtils.dp2px(context, height));
        setColorFilter(context.getResources().getColor(color), PorterDuff.Mode.SRC);
    }
}
