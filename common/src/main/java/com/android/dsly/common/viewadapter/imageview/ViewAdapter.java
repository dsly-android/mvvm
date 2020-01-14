package com.android.dsly.common.viewadapter.imageview;


import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.dsly.common.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

/**
 * Created by goldze on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter("avatarUrl")
    public static void setAvatarUrl(ImageView imageView, String avatarUrl) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .circleCrop();
        Glide.with(imageView.getContext()).load(avatarUrl).apply(options).into(imageView);
    }

    @BindingAdapter(value = {"url", "placeholderRes", "errRes", "width", "height"}, requireAll = false)
    public static void setImageUrl(ImageView imageView, String url, int placeholderRes, int errRes, Integer width, Integer height) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            RequestOptions options = new RequestOptions().placeholder(placeholderRes).error(errRes);
            RequestBuilder<Drawable> apply = Glide.with(imageView.getContext()).load(url).apply(options);
            if (width != null && height != null) {
                apply.override(width, height);
            }
            apply.into(imageView);
        }
    }

    @BindingAdapter("selected")
    public static void setSelected(ImageView imageView, boolean selected) {
        imageView.setSelected(selected);
    }
}