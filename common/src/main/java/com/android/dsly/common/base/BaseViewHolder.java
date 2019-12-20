package com.android.dsly.common.base;

import android.content.Context;
import android.view.View;

/**
 * @author 陈志鹏
 * @date 2019-09-26
 */
public abstract class BaseViewHolder<T> extends com.chad.library.adapter.base.BaseViewHolder {

    private Context mContext;

    public BaseViewHolder(View view) {
        super(view);
    }

    public void convert(Context context, T data){
        mContext = context;
    }
}
