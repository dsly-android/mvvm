package com.htxtdshopping.htxtd.frame.widget.groupheadview.listener;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface OnProgressListener {
    void onStart();

    void onComplete(ImageView iv, Bitmap bitmap);
}
