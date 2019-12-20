package com.android.dsly.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.dsly.common.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author 陈志鹏
 * @date 2018/10/29
 */
public final class GlideUtils {

    public static void loadAvatar(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .circleCrop();
        Glide.with(context).load(url).apply(options).into(iv);
    }

    public static void loadImage(Context context, String urlOrPath, ImageView iv) {
        loadImage(context, null, null, urlOrPath, iv);
    }

    public static void loadImage(Context context, Integer width, Integer height, String urlOrPath, ImageView iv) {
        loadImage(context, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, width, height, urlOrPath, iv);
    }

    public static void loadImage(Context context, int placeRes, int errRes, Integer width, Integer height, String urlOrPath, ImageView iv) {
        RequestOptions options = new RequestOptions().placeholder(placeRes).error(errRes);
        RequestBuilder<Drawable> apply = Glide.with(context).load(urlOrPath).apply(options);
        if (width != null && height != null) {
            apply.override(width, height);
        }
        apply.into(iv);
    }
}