package com.htxtdshopping.htxtd.frame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.htxtdshopping.htxtd.frame.R;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.CombineBitmap;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.layout.WechatLayoutManager;
import com.htxtdshopping.htxtd.frame.widget.groupheadview.listener.OnProgressListener;

import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * @author 陈志鹏
 * @date 2021/7/22
 */
public class AppGlideUtils {

    public static void loadGroupAvatar(Context context, List<String> urls, ImageView iv) {
        CombineBitmap.init(context)
                .setLayoutManager(new WechatLayoutManager())
                .setSize(100)
                .setGap(3)
                .setGapColor(ContextCompat.getColor(context, R.color._77ffffff))
                .setPlaceholder(R.drawable.ic_launcher_background)
                .setUrls(urls.toArray(new String[0]))
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(ImageView imageView, Bitmap bitmap) {
                        RequestOptions options = new RequestOptions()
                                .transform(new RoundedCorners(6));
                        Glide.with(context).load(bitmap).apply(options).into(imageView);
                    }
                }).setImageView(iv).build();
    }
}
