package com.android.dsly.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.android.dsly.common.R;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 陈志鹏
 * @date 2018/10/29
 */
public final class GlideUtils {

    public static void loadAvatar(Context context, String url, ImageView iv) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
//                .transform(new RoundedCorners(10)) //使用RoundedCorners，imageview的scaleType不能设置成CENTER_CROP
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

    /**
     * 加载图片生成bitmap对象
     */
    public static void loadImageToBitmap(Context context,String urlOrPath){
        Glide.with(context)
                .asBitmap()
                .load(urlOrPath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    /**
     * 清理图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (ThreadUtils.isMainThread()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(final Context context) {
        try {
            if (ThreadUtils.isMainThread()) {
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     * 主要调用这个方法
     */
    public static void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        FileUtils.delete(ImageExternalCatchDir);
    }
}