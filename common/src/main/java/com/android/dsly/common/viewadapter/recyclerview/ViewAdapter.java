package com.android.dsly.common.viewadapter.recyclerview;

import com.android.dsly.common.listener.OnSmartScrollListener;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 陈志鹏
 * @date 2020-01-11
 */
public class ViewAdapter {

    @BindingAdapter(value = {"scrollLoadImageEnabled"})
    public static void scrollLoadImageEnabled(RecyclerView recyclerView, boolean scrollLoadImageEnabled) {
        if (!scrollLoadImageEnabled) {
            recyclerView.addOnScrollListener(new OnSmartScrollListener(recyclerView.getContext()));
        }
    }
}