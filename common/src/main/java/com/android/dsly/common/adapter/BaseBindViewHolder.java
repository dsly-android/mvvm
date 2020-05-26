package com.android.dsly.common.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

/**
 * @author 陈志鹏
 * @date 2019-12-30
 */
public abstract class BaseBindViewHolder<T> extends BaseViewHolder {

    public BaseBindViewHolder(@NotNull View view) {
        super(view);
    }

    public abstract void convert(Context context, T data);
}
