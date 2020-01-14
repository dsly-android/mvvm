package com.android.dsly.common.viewadapter.recyclerview;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 陈志鹏
 * @date 2020-01-11
 */
public final class ViewAdapter {

    @BindingAdapter(value = {"scrollLoadImageEnabled"})
    public static void scrollLoadImageEnabled(RecyclerView recyclerView, boolean scrollLoadImageEnabled) {
        if (!scrollLoadImageEnabled) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            Glide.with(recyclerView.getContext()).resumeRequests();
                            break;
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            Glide.with(recyclerView.getContext()).pauseRequests();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}
